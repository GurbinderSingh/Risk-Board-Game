package view.gui;

import controller.Controller;
import model.dataread.ReadMap;
import model.logic.Country;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MapFrame extends JFrame
{

    private Controller drawController;



    public MapFrame(ReadMap readMap, List<Country> countries)
    {
        drawController = new Controller(readMap, countries);

        drawController.getMapGUI().setBackground(new Color(9, 162, 190));
        this.setTitle("AllThoseTerritories");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1250, 650);

        this.getContentPane().add(drawController.getMapGUI());
    }
}
