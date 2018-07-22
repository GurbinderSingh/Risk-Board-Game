package view.gui;


import model.dataread.ReadMap;
import model.logic.Country;
import model.logic.Territory;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class MapGUI extends JPanel
{

    private JPanel mainPanel;
    private List<Country> countryMap;
    private List<Territory> territoryList;
    private final static int ZONELeft = 275;
    private final static int ZONERight = 900;
    private JLabel[] showarmystrength;
    private JButton endRound;
    private JLabel status;
    private ReadMap readMap;



    public MapGUI(ReadMap readMap, List<Country> countryMap)
    {

        this.readMap = readMap;
        mainPanel = new JPanel();
        this.countryMap = countryMap;

        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        this.territoryList = readMap.getList_territory();
        endRound = new JButton("End Round");
        status = new JLabel("");
        showarmystrength = new JLabel[countryMap.size()];


        for(int label = 0; label < showarmystrength.length; label++)
        {
            showarmystrength[label] = new JLabel("");
        }
        this.setLayout(null);

        status.setFont(new Font("Dialog", Font.BOLD, 16));
        status.setBounds(400, 590, 1250, 30);
        this.add(status);

        endRound.setBounds(1120, 590, 100, 25);
        this.add(endRound);
    }



    public JLabel getStatus()
    {
        return status;
    }



    public JButton getEndround()
    {
        return endRound;
    }



    @Override
    public void paintComponent(Graphics g)
    {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        for(Country country : countryMap)
        {

            country.drawRoad(g2d, ZONELeft, ZONERight);
        }

        for(Territory territory : territoryList)
        {

            territory.drawTerritory(g2d);
        }
        for(Country country : countryMap)
        {
            country.drawArmyStrength(g2d);
        }

        readMap.drawMessage(g2d);
    }
}
