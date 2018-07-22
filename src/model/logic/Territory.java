package model.logic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Territory
{
    private String territoryname;
    private Color color;
    private Polygon polygon;
    private List<Country> neighbors;
    private boolean isTaken;
    private int capital_X, capital_Y;



    public Territory(String territoryname, List<Integer> coordinates)
    {
        this.territoryname = territoryname;
        color = Color.GRAY;
        polygon = initPolygon(coordinates);
        neighbors = new ArrayList<>();
        this.capital_X = 0;
        this.capital_Y = 0;
        this.isTaken = false;
    }



    public String getTerritoryname()
    {
        return territoryname;
    }



    public void setColor(Color color)
    {
        this.color = color;
    }



    public Polygon getPolygon()
    {
        return polygon;
    }



    public List<Country> getNeigbors()
    {
        return neighbors;
    }



    public void setCapital_X(int capital_X)
    {
        this.capital_X = capital_X;
    }



    public void setCapital_Y(int capital_Y)
    {
        this.capital_Y = capital_Y;
    }



    public boolean isTaken()
    {
        return isTaken;
    }



    public void setIsTaken(boolean isTaken)
    {
        this.isTaken = isTaken;
    }



    private Polygon initPolygon(List<Integer> coordinates)
    {
        int arraylength = coordinates.size() / 2;
        int[] xcoordinates = new int[arraylength];
        int[] ycoordinates = new int[arraylength];

        for(int i = 0, index = 0; i < coordinates.size(); i += 2, index++)
        {
            if(index < arraylength)
            {
                xcoordinates[index] = coordinates.get(i);
                ycoordinates[index] = coordinates.get(i + 1);
            }
        }
        return new Polygon(xcoordinates, ycoordinates, arraylength);
    }



    public void drawTerritory(Graphics2D graphics)
    {

        graphics.setColor(this.color);
        graphics.fillPolygon(this.polygon);
        graphics.setColor(Color.DARK_GRAY);
        graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.drawPolygon(this.polygon);
    }
}
