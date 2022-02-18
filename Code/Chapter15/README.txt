This directory contains two 3D example renderer/viewers and 3D models.

The Cosmic directory contains a Java3D based model viewer.  
The Jist3D directory contains the Jist3D (JOGL based) render engine and viewer.

Both viewers can load any of the models provided in the Models directory.  The model formats and loaders included are .rtg, .flt, and .x.
The rest of the files are textures or animations.

To run all the examples Java3D needs to be installed, see below.
----------

Cosmic Viewer
----------
“launchViewer*”
This viewer exposes many but not all of Java3D's scene controls.
It also allows viewing of animations in the .x models that have them. Additionally, for those .x models, a cel-shader effect can be applied both from the menus

Mouse controls.
Left Mouse 			= Tumble Camera
Middle Mouse		= Translate Camera
Right Mouse + Up/Down	= Zoom Camera in and out

Jist3D
----------
Jist3D is a JOGL based render engine based on this chapters design and capable of rendering certain Java3D scene graphs.  This is done by loading a Java3D scene graph and then converting to the Jist3D graph classes as in the examples.

“launchViewer*”
Although the Jist3D viewer is JOGL based and makes no direct use of Java3D, the “launchViewer” example use Java3D model loaders.  The scene/model is loaded by a Java3D loader and then converted to Jist3D specific data for rendering.
Several examples show some command-line usages and the source is provided as companion to the chapter.

“rendererSpherePerfTest*”
rendererSpherePerfTest is a simpler app that does not make use of Java3D therefore is a good starting place for new apps and also shows how to make geometry directly without a loader or how a new loader would need to make it.
Both Jist3D viewer apps uses the same mouse controls.
Left Mouse 			= Spin and move object in Y
Middle Mouse + Up/Down	= Scale (Zoom) object up or down
Middle Mouse + Up/Down + SHIFT = Increase/Decrease FOV
Right Mouse 		= Tumble Camera

Hotkeys
F1		Regular camera view render
F2		Light viewer
F3		Depth Map from light viewer
F4		scene light depth map textured
F5		scene light depth textured
F6		Camera view depth mapped shadowed 
			(when support, requires OpenGL 1.4)
T		enable/disable textures


For more information, bug fixes, additional documentation and future releases please visit.
www.imilabs.com
and
www.javagaming.org

