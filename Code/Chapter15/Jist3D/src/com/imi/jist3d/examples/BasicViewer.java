/*
 * Copyright (c) 2004, Immediate Mode Interactve, LLC. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * - Neither the name of Immediate Mode Interactve, LLC. or the names of
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. IMMEDIATE
 * MODE INTERACTIVE, LLC. ("IMI") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL IMI OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * IMI HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package com.imi.jist3d.examples;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.*;
import com.imi.j3d.loaders.rtg.*;
import com.imi.j3d.loaders.flt.*;
import java.util.*;
import com.imi.j3d.util.*;
import net.java.games.jogl.*;
import javax.swing.tree.*;

import com.imi.jist3d.render.BasicRenderer;
import com.imi.jist3d.render.RenderShape;
import com.imi.jist3d.render.RendererUtils;
import com.imi.jist3d.render.Scene;
import com.imi.jist3d.render.RenderBinNode;
import com.imi.jist3d.scenegraph.*;
import com.imi.jist3d.gui.*;
import com.imi.jist3d.util.FrameRateBehavior;
import com.imi.util.*;
import com.imi.util.Debug;

public class BasicViewer implements KeyListener
{
	// Viewer fields
	Frame myFrame = null;
	GridBagLayout gridbag = new GridBagLayout();
	Panel mainPanel = null;
	JScrollPane sceneGraphPanel = null;
	JScrollPane renderGraphPanel = null;
	boolean showTree = true;
	boolean firstLoad = true;
	JFileChooser jloadFileDialog;
	static final int FLT = 0;
	static final int GRD = 1;
	static final int RTG = 2;
	static final int XFILE = 3;
	String lastFileType = "";
	String lastFilename = null;

	// Renderer fields
	BasicRenderer renderer = null;
	BasicRenderLoop renderLoop = null;
	Scene scene = new Scene();
	AnimationController animationControl = null;
	MouseConstrainedTumble mct = null;
	FrameRateBehavior frb;
	float lightAngle = 135.0f * (float)Math.PI / 180.0f;
	float lightHeight = 8.0f;

	public static void main(String[] args)
	{
		BasicViewer viewer = new BasicViewer(args);
	}

	private MenuBar getMainMenuBar()
	{
		MenuBar MB = new MenuBar();
		MB.add(getFileMenu());
		MB.add(getOptionsMenu());
		return MB;
	}

	private Menu getFileMenu()
	{
		Menu M = getMenu("File");
		MenuItem mi;
		mi = (MenuItem)M.add(new MenuItem("Open File"));
		mi.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ce)
			{
				openFile(true);
			}
		});
		mi = (MenuItem)M.add(new MenuItem("Merge File"));
		mi.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ce)
			{
				openFile(false);
			}
		});
		mi = (MenuItem)M.add(new MenuItem("Quit"));
		mi.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ce)
			{
				renderLoop.stop();
				System.exit(0);
			}
		});
		return M;
	}

	private Menu getOptionsMenu()
	{
		Menu M = getMenu("Options");

		MenuItem mi;
		final CheckboxMenuItem showTreeCMI = new CheckboxMenuItem("Show Tree");
		showTreeCMI.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent event)
			{
				showTree(showTreeCMI.getState());
			}
		});
		M.add(showTreeCMI);
		final CheckboxMenuItem skyboxCMI = new CheckboxMenuItem("Render Skybox");
		skyboxCMI.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent event)
			{
				renderer.getScene().getSkyBox().setEnable(skyboxCMI.getState());
			}
		});
		M.add(skyboxCMI);
		return M;
	}

	private Menu getMenu(String name)
	{
		Menu M = new Menu(name);
		return M;
	}

	public BasicViewer(String[] args)
	{
		Frame frame = new Frame("Basic Jist3D Viewer");
		myFrame = frame;
		myFrame.setMenuBar(getMainMenuBar());
		jloadFileDialog = new JFileChooser();
		ExampleFileFilter filter = new ExampleFileFilter("flt", "OpenFLT File");
		jloadFileDialog.addChoosableFileFilter(filter);
		filter = new ExampleFileFilter("rtg", "Maya RTG File");
		jloadFileDialog.addChoosableFileFilter(filter);
		filter = new ExampleFileFilter("x", "X File");
		jloadFileDialog.addChoosableFileFilter(filter);
		File file = new File("." + System.getProperty("file.separator"));
		jloadFileDialog.setCurrentDirectory(file);
		jloadFileDialog.setFileFilter(jloadFileDialog.getAcceptAllFileFilter());

		// hack to make libs load in right order
		// to prevent jawt.dll conflicts
		javax.media.j3d.TransformGroup tgGarb = new javax.media.j3d.TransformGroup();
		javax.media.j3d.Transform3D t3dGarb = new javax.media.j3d.Transform3D();
		t3dGarb.setTranslation(new javax.vecmath.Vector3f(0.0f, -9.75f, 0.0f));
		tgGarb.setTransform(t3dGarb);

		// main render loop stuff		
		renderer = new BasicRenderer();
		renderLoop = new BasicRenderLoop(renderer);
		renderer.setScene(scene);
		GLCanvas canvas = renderer.getCanvas();
		renderer.getScene().getSkyBox().setEnable(false);

		RtgLoader rtgLoader = new RtgLoader();
		for (int i = 0; i < args.length; i++)
		{
			String filename = args[i];
			if (filename.equals("-skybox"))
			{
				renderer.getScene().getSkyBox().setEnable(true);
			}
			if (filename.endsWith(".rtg"))
			{
				rtgLoader.setFlags(rtgLoader.getFlags() | rtgLoader.PRINT_STATS);
				try
				{
					com.sun.j3d.loaders.Scene sceneBase = rtgLoader.load(filename);
					Node node =
						ConvertUtils.makeJist3DGraphFromJava3DGraph(sceneBase.getSceneGroup());
					SceneGraphUtils.loadBindBin(renderer.bindBin, node);
					scene.getSceneList().add(node);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else if (filename.endsWith(".flt"))
			{
				loadFltFile(filename);
			}		
			else if (filename.endsWith(".x"))
			{
				loadXFile(filename);
			}		
		}

		sceneGraphPanel = new JScrollPane();
		sceneGraphPanel.setMinimumSize(new Dimension(140, 100));

		renderGraphPanel = new JScrollPane();
		renderGraphPanel.setMinimumSize(new Dimension(140, 100));

		mainPanel = new Panel();
		mainPanel.setLayout(gridbag);
		GUIConstraints.constrain(
			mainPanel,
			sceneGraphPanel,
			0,
			0,
			1,
			1,
			GridBagConstraints.BOTH,
			GridBagConstraints.NORTHWEST,
			1.0,
			1.0,
			0,
			0,
			0,
			0);

		GUIConstraints.constrain(
			mainPanel,
			renderGraphPanel,
			0,
			1,
			1,
			1,
			GridBagConstraints.BOTH,
			GridBagConstraints.NORTHWEST,
			1.0,
			1.0,
			0,
			0,
			0,
			0);

		mainPanel.setSize(new Dimension(140, 100));

		frame.add(mainPanel, BorderLayout.WEST);

		Panel tmpPanel = new Panel();
		tmpPanel.setLayout(gridbag);
		GUIConstraints.constrain(
			tmpPanel,
			canvas,
			0,
			0,
			1,
			1,
			GridBagConstraints.BOTH,
			GridBagConstraints.CENTER,
			1.0,
			1.0,
			1,
			1,
			1,
			1);

		tmpPanel.setBackground(Color.black);

		frame.add(tmpPanel, BorderLayout.CENTER);
		Dimension dim = frame.getPreferredSize();
		dim.width = 800;
		dim.height += 100;
		frame.setSize(dim);

		EngineUpdate preCall = new EngineUpdate()
		{
			public void update()
			{
				updateMatrices();
				if (animationControl != null)
					animationControl.update();
			}
		};
		renderLoop.preUpdate = preCall;

		frb = new FrameRateBehavior();
		frb.initialize();
		EngineUpdate postCall = new EngineUpdate()
		{
			public void update()
			{
				frb.update();
			}
		};
		renderLoop.postUpdate = postCall;

		mct = new MouseConstrainedTumble(scene);
		canvas.addMouseListener(mct);
		canvas.addMouseMotionListener(mct);
		canvas.addKeyListener(this);

		frame.setSize(800, 600);

		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				renderLoop.stop();
				System.exit(0);
			}
		});
		frame.setMenuBar(getMainMenuBar());

		frame.show();

		renderLoop.start();
	}

	public String openFile(boolean empty)
	{
		String filename = null;
		int returnVal = jloadFileDialog.showOpenDialog(myFrame);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File theFile = jloadFileDialog.getSelectedFile();
			if (theFile == null)
			{
				JOptionPane.showMessageDialog(
					null,
					"File Not Found",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				if (theFile.isDirectory())
				{
					JOptionPane.showMessageDialog(
						null,
						"Can not load a directoy",
						"Error",
						JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					filename =
						jloadFileDialog.getCurrentDirectory().getPath()
							+ System.getProperty("file.separator")
							+ jloadFileDialog.getSelectedFile().getName();
					if (filename == "")
					{
						JOptionPane.showMessageDialog(
							null,
							"Empty file name",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						String filetype = getFileType(theFile);
						load(filetype, filename, empty);
						lastFileType = filetype;
						lastFilename = filename;
					}
				}
			}
		}
		return filename;
	}

	public String getFileType(File file)
	{
		String ext = null;
		String filename = file.getName();
		int i = filename.lastIndexOf('.');

		if (i > 0 && i < filename.length() - 1)
		{
			ext = filename.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	public void load(String filetype, String filename, boolean empty)
	{
		final String type = filetype;
		final String name = filename;
		//statusLabel.setText("Loading...");
		try
		{
			renderer.pause();
			// Bug in clearing empty scene
			if (empty && !firstLoad)
			{
				scene.getSceneList().clear();
				((ArrayList)renderer.drawListOpaque.getBin()).clear();
				renderer.drawListTransparent.clear();
			}

			System.out.println("Loading type " + type);
			if (type.equals("flt"))
				loadFltFile(name);
			if (type.equals("rtg"))
				loadRtgFile(name);
			if (type.equals("x"))
				loadXFile(name);
			//statusLabel.setText("Done loading.");
			//statusLabel.repaint();
			//resetView();
			firstLoad = false;

			renderer.resume();
			setTree(scene);
		}
		catch (Exception e)
		{
		}
	}

	public void loadRtgFile(String filename)
	{
		RtgLoader rtgLoader = new RtgLoader();
		rtgLoader.setFlags(0);
		//if ( stats )
		{
			rtgLoader.setFlags(RtgLoader.PRINT_STATS);
		}
		//if ( displayTextures )
		{
			rtgLoader.setFlags(rtgLoader.getFlags() | RtgLoader.USE_TEXTURES);
		}
		try
		{
			com.sun.j3d.loaders.Scene sceneBase = rtgLoader.load(filename);
			Node node = ConvertUtils.makeJist3DGraphFromJava3DGraph(sceneBase.getSceneGroup());
			SceneGraphUtils.loadBindBin(renderer.bindBin, node);
			scene.getSceneList().add(node);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(
				null,
				"File Not Found",
				"Error",
				JOptionPane.ERROR_MESSAGE);
		}
	}

	public void loadFltFile(String filename)
	{
		System.out.println("Loading file " + filename);
		FltLoader fltLoader = new FltLoader();
		//fltLoader.setTexturePath("C:\\work\\Projects\\JOGL Tests\\Models\\");
		//fltLoader.setFlags( 0 );
		try
		{
			// uber hack for text examples!!
			com.sun.j3d.loaders.Scene sceneBase = fltLoader.load(filename);
			Node node;
			if (fltLoader.getShortFilename().equals("citywalk.flt"))
			{
				javax.media.j3d.TransformGroup tg = new javax.media.j3d.TransformGroup();
				javax.media.j3d.Transform3D t3d = new javax.media.j3d.Transform3D();
				t3d.setTranslation(new javax.vecmath.Vector3f(0.0f, -9.86f, 0.0f));
				tg.setTransform(t3d);
				tg.addChild(sceneBase.getSceneGroup());
				node = ConvertUtils.makeJist3DGraphFromJava3DGraph(tg);
			}
			else
			{
				node = ConvertUtils.makeJist3DGraphFromJava3DGraph(sceneBase.getSceneGroup());
			}
			SceneGraphUtils.loadBindBin(renderer.bindBin, node);
			scene.getSceneList().add(node);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(
				null,
				"File Not Found",
				"Error",
				JOptionPane.ERROR_MESSAGE);
		}
	}

	public void loadXFile(String filename)
	{
		XLoader xLoader = new XLoader();
		xLoader.setFlags(0);
		//if ( stats )
		{
			xLoader.setFlags(XLoader.PRINT_STATS);
		}
		//if ( displayTextures )
		{
			xLoader.setFlags(xLoader.getFlags() | XLoader.USE_TEXTURES);
		}
		try
		{
			com.sun.j3d.loaders.Scene sceneBase = xLoader.load(filename);
			Node node = ConvertUtils.makeJist3DGraphFromJava3DGraph(sceneBase.getSceneGroup());
			SceneGraphUtils.loadBindBin(renderer.bindBin, node);
			scene.getSceneList().add(node);

			if (scene != null)
			{

				if (xLoader.animationController != null)
				{
					try
					{
						xLoader.animationController.loop = false;
						xLoader.animationController.setIdle(0);
						xLoader.animationController.playIdle();

						xLoader.animationController.init();
						animationControl = xLoader.animationController;
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					System.out.println("=====X file loader==== no animation controller");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(
				null,
				"File Not Found",
				"Error",
				JOptionPane.ERROR_MESSAGE);
		}
	}

	public void showTree(boolean state)
	{
		setTree(scene);
		if (state)
		{
			mainPanel.setVisible(true);
			myFrame.validate();
		}
		else
			hideTree();
		showTree = state;
	}

	public boolean isShowTree()
	{
		return showTree;
	}

	public void hideTree()
	{
		mainPanel.setVisible(false);
		myFrame.validate();
	}

	public void setTree(Scene sceneIn)
	{
		sceneGraphPanel.setViewportView(displayTree(sceneIn));
		renderGraphPanel.setViewportView(displayTree(renderer.drawListOpaque));
	}

	public JTree displayTree(Scene sceneIn)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Scene");
		for (int i = 0; i < sceneIn.getSceneList().size(); i++)
		{
			node.add(getTreeBranches(sceneIn.getSceneList().get(i)));
		}

		final JTree tree;
		tree = new JTree(node);

		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(new ImageIcon("images/orb.gif"));
		renderer.setOpenIcon(new ImageIcon("images/open.gif"));
		renderer.setClosedIcon(new ImageIcon("images/closed.gif"));
		tree.setCellRenderer(renderer);

		tree.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					TreePath selPath = tree.getSelectionPath();
					if (selPath != null)
					{
						Object object =
							((DefaultMutableTreeNode)selPath.getLastPathComponent())
								.getUserObject();
						Debug.println("Selected object: " + object);
						if (object instanceof javax.media.j3d.Node)
						{
							Debug.println("Selected object: " + object);
							if (((javax.media.j3d.Node)object).isCompiled())
								Debug.println("Compiled");
							else
								Debug.println("Not Compiled");
						}
						if (object instanceof com.imi.jist3d.scenegraph.TransformGroup)
						{
							Debug.println("Selected TransformGroup: " + object);
							//setManipulator( (TransformGroup)object );
						}
					}
				}
			}
		});
		return tree;
	}

	static public DefaultMutableTreeNode getTreeBranches(Object node)
	{
		if (node == null)
			return null;
		String name = node.toString();
		//System.out.println(name);
		int index = name.indexOf('@');
		DefaultMutableTreeNode treeNode;
		if (node instanceof FLTDOF)
			treeNode = new DefaultMutableTreeNode(node);
		else if (node instanceof ArrayList)
			treeNode = new DefaultMutableTreeNode("ArrayList");
		else if (index != -1)
			treeNode = new DefaultMutableTreeNode(name.substring(name.lastIndexOf('.') + 1, index));
		else
			treeNode = new DefaultMutableTreeNode(name);

		if (node instanceof com.imi.jist3d.scenegraph.Group)
		{
			com.imi.jist3d.scenegraph.Group group = (com.imi.jist3d.scenegraph.Group)node;
			try
			{
				for (int i = 0; i < group.getChildren().size(); i++)
				{
					Object node2 = group.getChildren().get(i);
					if (node2 != null)
					{
						treeNode.add(getTreeBranches(node2));
					}
				}
			}
			catch (Exception e)
			{
				Debug.println("Can't descend node " + node + " due to " + e);
				//e.printStackTrace();
			}
		}
		if (node instanceof RenderBinNode)
		{
			//treeNode.add(getTreeBranches(((RenderBinNode)node).getBin()));
			Object object = ((RenderBinNode)node).getBin();
			if (object instanceof RenderShape)
			{
				treeNode.add(getTreeBranches(((RenderBinNode)node).getBin()));
			}
			else if (object instanceof ArrayList)
			{
				ArrayList group = (ArrayList)object;
				try
				{
					for (int i = 0; i < group.size(); i++)
					{
						Object node2 = group.get(i);
						if (node2 != null)
						{
							treeNode.add(getTreeBranches(node2));
						}
					}
				}
				catch (Exception e)
				{
					Debug.println("Can't descend node " + node + " due to " + e);
					//e.printStackTrace();
				}
			}
		}
		if (node instanceof RenderShape)
		{
			try
			{
				//javax.media.j3d.Geometry geo = ((Shape3D)node).getGeometry();
				String nameG = ((RenderShape)node).vertexArraySet.vertexArray.toString();
				//int indexG = nameG.indexOf('@');
				DefaultMutableTreeNode geoNode;
				//if ( indexG != -1 )
				//	geoNode = new DefaultMutableTreeNode(nameG.substring(nameG.lastIndexOf('.') + 1, indexG));
				//else
				geoNode = new DefaultMutableTreeNode(nameG);
				treeNode.add(geoNode);
			}
			catch (Exception e)
			{
				Debug.println("Can't descend node " + node + " due to " + e);
				//e.printStackTrace();
			}
		}
		return treeNode;
	}

	public JTree displayTree(RenderBinNode sceneIn)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Scene");
		for (int i = 0; i < ((ArrayList)sceneIn.getBin()).size(); i++)
		{
			node.add(getTreeBranches(((ArrayList)sceneIn.getBin()).get(i)));
		}

		final JTree tree;
		tree = new JTree(node);

		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(new ImageIcon("images/orb.gif"));
		renderer.setOpenIcon(new ImageIcon("images/open.gif"));
		renderer.setClosedIcon(new ImageIcon("images/closed.gif"));
		tree.setCellRenderer(renderer);

		return tree;
	}

	void updateMatrices()
	{
		//System.out.println(15*Math.sin(eyeAngle));
		mct.update();

		scene.getLight().setLightVector(
			(float) (8.0 * Math.sin(lightAngle)),
			lightHeight,
			(float) (8 * Math.cos(lightAngle)),
			1.0f);
	}

	public void keyTyped(KeyEvent e)
	{
	}

	public void keyPressed(KeyEvent e)
	{
	}

	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		switch (key)
		{
			case KeyEvent.VK_F1 :
				renderer.drawMode = 0;
				break;
			case KeyEvent.VK_F2 :
				renderer.drawMode = 1;
				break;
			case KeyEvent.VK_F3 :
				renderer.drawMode = 2;
				break;
			case KeyEvent.VK_F4 :
				renderer.drawMode = 3;
				break;
			case KeyEvent.VK_F5 :
				renderer.drawMode = 4;
				break;
			case KeyEvent.VK_F6 :
				renderer.drawMode = 5;
				break;
			case KeyEvent.VK_W :
				renderer.setGlobalWireFrameState(!renderer.isGlobalWireFrameState());
				System.out.println("Switching wireframe state");
				break;
			case KeyEvent.VK_T :
				renderer.setGlobalTextureState(!renderer.isGlobalTextureState());
				System.out.println("Switching wireframe state");
				break;
			case KeyEvent.VK_S :
				renderLoop.stop();
				break;
			case KeyEvent.VK_SPACE :
				renderLoop.start();
				break;
		}
	}
}