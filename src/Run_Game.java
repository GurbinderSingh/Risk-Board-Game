import model.dataread.ReadMap;
import model.logic.Country;
import view.gui.MapFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class Run_Game
{
    public static void main(String[] args)
    {

        if(args.length == 1)
        {
            String path = args[0];
            ReadMap readMap = new ReadMap(path);
            List<Country> countries = readMap.readfile();

            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {

                    MapFrame frame = new MapFrame(readMap, countries);
                    frame.setVisible(true);
                }
            });
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                    "It is not a Valid Path",
                    "Path Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}