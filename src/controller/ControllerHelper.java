package controller;

import model.logic.Territory;
import view.gui.MapGUI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;


public class ControllerHelper
{

    public void showCountry(MapGUI mapGUI, List<Territory> listofterrirtory, MouseEvent e)
    {
        Point mousepoint = e.getPoint();

        for(int i = 0; i < listofterrirtory.size(); i++)
        {
            if(listofterrirtory.get(i).getPolygon().contains(mousepoint) && !listofterrirtory.get(i).isTaken())
            {
                mapGUI.getStatus().setText("Claim: " + listofterrirtory.get(i).getTerritoryname());

                for(int j = 0; j < listofterrirtory.size(); j++)
                {
                    if(listofterrirtory.get(i).getTerritoryname().equals(listofterrirtory.get(j).getTerritoryname())
                            && !listofterrirtory.get(j).isTaken())
                    {
                        listofterrirtory.get(j).setColor(Color.blue);
                    }
                    else if(!listofterrirtory.get(j).isTaken())
                    {
                        listofterrirtory.get(j).setColor(Color.GRAY);
                    }
                }
            }
        }
        mapGUI.repaint();
    }
}
