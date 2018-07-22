package model.logic;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Country
{
    private String name;
    private boolean taken;
    private List<Territory> territories;
    private List<Country> neighbors;
    private int armyStrength, capital_X, capital_Y;



    public Country(String name, int capital_X, int capital_Y)
    {
        this.name = name;
        this.taken = false;
        this.territories = new ArrayList<>();
        this.neighbors = new ArrayList<>();
        this.armyStrength = 0;
        this.capital_X = capital_X;
        this.capital_Y = capital_Y;
    }



    public String getName()
    {
        return name;
    }



    public boolean isTaken()
    {
        return taken;
    }



    public void setTaken(boolean taken)
    {
        this.taken = taken;
    }



    public List<Territory> getTerritories()
    {
        return territories;
    }



    public List<Country> getNeighbors()
    {
        return neighbors;
    }



    public int getArmyStrength()
    {
        return armyStrength;
    }



    public void setArmyStrength(int armyStrength)
    {
        this.armyStrength = armyStrength;
    }



    public int getCapital_X()
    {
        return capital_X;
    }



    public int getCapital_Y()
    {
        return capital_Y;
    }



    public void drawRoad(Graphics2D graphics, int zoneLeft, int zoneRight)
    {
        for(Country neighbor : this.neighbors)
        {
            graphics.setStroke(new BasicStroke(2));
            graphics.setColor(Color.lightGray);
            int[] capital_2 = new int[2];
            capital_2[0] = neighbor.capital_X;
            capital_2[1] = neighbor.capital_Y;

            int temp_x = 1250 - Math.abs(this.capital_X - capital_2[0]);

            if(!((this.capital_X <= zoneLeft && capital_2[0] <= zoneLeft) || (this.capital_X >= zoneRight && capital_2[0] >= zoneRight)) &&
                    (this.capital_X <= zoneLeft || this.capital_X >= zoneRight) && (capital_2[0] <= zoneLeft || capital_2[0] >= zoneRight))
            {
                graphics.draw(new Line2D.Double(this.capital_X, this.capital_Y, (this.capital_X >= zoneRight) ? this.capital_X + temp_x : this.capital_X - temp_x, capital_2[1]));
                graphics.draw(new Line2D.Double(capital_2[0], capital_2[1], (this.capital_X >= zoneRight) ? capital_2[0] - temp_x : capital_2[0] + temp_x, this.capital_Y));
            }
            else
            {
                graphics.draw(new Line2D.Double(this.capital_X, this.capital_Y,
                        neighbor.capital_X,
                        neighbor.capital_Y));
            }
        }
    }



    public void drawArmyStrength(Graphics2D g2d)
    {

        Rectangle2D stringBounds = g2d.getFontMetrics().getStringBounds("" + this.armyStrength, g2d);
        g2d.setFont(new Font("default", Font.BOLD, 12));
        g2d.setColor(Color.WHITE);
        g2d.drawString("" + this.armyStrength, (int) (this.capital_X - stringBounds.getCenterX()), (int) (this.capital_Y - stringBounds.getCenterY()));
    }
}
