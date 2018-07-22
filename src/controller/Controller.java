package controller;

import model.dataread.ReadMap;
import model.logic.Country;
import model.logic.Phases;
import model.logic.Territory;
import view.gui.MapGUI;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class Controller implements MouseListener, MouseMotionListener, ActionListener
{

    private ReadMap readData;
    private MapGUI mapGUI;
    private List<Territory> listOfTerritories;
    private List<Country> listOfCountries;

    private ControllerHelper controllerHelper;
    private LogicController logicController;
    private boolean phase1;
    private int playRound;
    private boolean helperBoolean;
    private Phases phases;
    private Phases phaseshelper;



    public Controller(ReadMap readData, List<Country> countries)
    {
        this.readData = readData;
        this.mapGUI = new MapGUI(this.readData, countries);
        this.listOfCountries = new ArrayList<>();
        this.listOfCountries = countries;

        controllerHelper = new ControllerHelper();
        logicController = new LogicController();

        phase1 = false;

        playRound = 0;
        helperBoolean = false;
        phases = Phases.PHASE_ONE;
        phaseshelper = Phases.NO_CHANGE;

        listOfTerritories = this.readData.getList_territory();
        this.mapGUI.addMouseListener(this);

        this.mapGUI.addMouseMotionListener(this);
        this.mapGUI.getEndround().addActionListener(this);
        this.mapGUI.repaint();
    }



    public MapGUI getMapGUI()
    {
        return mapGUI;
    }



    @Override
    public void mouseClicked(MouseEvent e)
    {

        int divider = logicController.getPlayers().size();
        if(phases == Phases.PHASE_ONE)
        {
            phases = logicController.claimfunction(readData.getList_continent(), listOfCountries, listOfTerritories, mapGUI, e);
        }
        else
        {
            phase1 = true;
        }
        if(phases == Phases.PHASE_TWO)
        {
            phase1 = false;
            phaseshelper = Phases.PHASE_TWO;
            phases = logicController.setArmyForcement(readData, listOfTerritories, mapGUI, e);

            if(phases == Phases.PHASE_THREE && playRound % divider == 0)
            {
                mapGUI.getEndround().setEnabled(true);
            }
        }
        String won = "You Won!";
        String lost = "You Lost.";
        if(phases == Phases.PHASE_THREE && playRound % divider == 1)
        {
            if(logicController.attackComputer(readData, playRound % divider))
            {
                playRound++;

                String output = "";
                Phases gstatus = logicController.won(logicController.getPlayers().get(1), listOfCountries);
                if(gstatus == Phases.WON)
                {
                    output = won;
                }
                else if(gstatus == Phases.LOST)
                {
                    output = lost;
                }
                mapGUI.getStatus().setText(output);

                logicController.calculateReinformcement(readData.getList_continent(), mapGUI);
                phases = Phases.PHASE_TWO;
            }
        }
        else if(phases == Phases.PHASE_THREE && playRound % divider == 0 && helperBoolean)
        {
            helperBoolean = false;
            mapGUI.getStatus().setText("Select two countries for an assault or to move the army(>1)");
            String test = logicController.moveandattack(readData, listOfCountries, listOfTerritories, mapGUI, e);


            String output = "";
            Phases gstatus = logicController.won(logicController.getPlayers().get(0), listOfCountries);
            if(gstatus == Phases.WON)
            {
                output = won;
                JOptionPane.showMessageDialog(null,
                        "You won the game!",
                        "Game finished.",
                        JOptionPane.INFORMATION_MESSAGE);
                mapGUI.getEndround().setEnabled(false);
            }
            else if(gstatus == Phases.LOST)
            {
                output = lost;
            }
            mapGUI.getStatus().setText(output);
        }
        mapGUI.repaint();
    }



    @Override
    public void mousePressed(MouseEvent e)
    {

    }



    @Override
    public void mouseReleased(MouseEvent e)
    {

    }



    @Override
    public void mouseEntered(MouseEvent e)
    {

    }



    @Override
    public void mouseExited(MouseEvent e)
    {

    }



    @Override
    public void mouseDragged(MouseEvent e)
    {

    }



    @Override
    public void mouseMoved(MouseEvent e)
    {
        controllerHelper.showCountry(mapGUI, listOfTerritories, e);
        if(phase1 && phaseshelper == Phases.NO_CHANGE)
        {
            for(Country country : listOfCountries)
            {
                country.setArmyStrength(1);
            }
        }
        if(phases == Phases.PHASE_THREE)
        {
            helperBoolean = true;
        }
        mapGUI.repaint();
    }



    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == mapGUI.getEndround())
        {
            playRound++;

            phases = Phases.PHASE_TWO;
            helperBoolean = false;
            logicController.calculateReinformcement(readData.getList_continent(), mapGUI);
            mapGUI.getEndround().setEnabled(false);
        }
    }
}
