package model.logic;

import java.util.ArrayList;
import java.util.List;


public class Continent
{
    private String continentName;
    private int armyBonus;
    private List<Country> list_countries;



    public Continent(String continentName, int armyBonus)
    {
        this.continentName = continentName;
        this.armyBonus = armyBonus;
        this.list_countries = new ArrayList<>();
    }



    public int getArmyBonus()
    {
        return armyBonus;
    }



    public List<Country> getList_countries()
    {
        return list_countries;
    }
}
