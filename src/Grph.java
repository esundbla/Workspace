import java.awt.*;

public class Grph {
    public static void main(String[] args) {
        GraphicsEnvironment gfxEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screenDevList = gfxEnv.getScreenDevices();
        GraphicsDevice defaultDevice = gfxEnv.getDefaultScreenDevice();
        DisplayMode[] displayModes = defaultDevice.getDisplayModes();

        boolean fullScreenMode = false;
        if(defaultDevice.isFullScreenSupported()){
            fullScreenMode = true;
        }else
            System.out.println("full screen unsupported");
            FullScreenFrame1 myFrame = null;
            if(fullScreenMode && !forceWindowedMode)
        myFrame = new FullScreenFrame1("FullSCreenFrame1 Full Screen Mode", displayModes[1]);
            else
        myFrame = new FullScreenFrame1("FullScreenFrame1 Windowed Mode", false);
            myFrame.initToScreen();
    }
}

public class FullScreenFrame1 {
    public FullScreenFrame1(String title, DisplayMode mode) throws
    HeadlesException {
        super(title);
        newDisplayMode = mode;
        setUndecorated(true);
        setIgnoreRepaint(true);



    }
}