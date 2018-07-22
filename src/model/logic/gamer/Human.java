package model.logic.gamer;

import model.logic.Continent;
import model.logic.Country;
import model.logic.Phases;
import model.logic.Territory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Human implements Player
{
    private List<Country> countries;

    private int armies;



    public Human()
    {
        countries = new ArrayList<>();
        armies = 0;
    }



    public void claim(Country country)
    {
        countries.add(country);
    }



    @Override
    public int calculatearmyreinforcement()
    {
        int value = (countries.size() / 3);
        return value;
    }



    @Override
    public int hasContinent(List<Continent> continents)
    {
        int army = 0;
        for(Continent continent : continents)
        {
            int armytrue = 0;
            for(Country countryhuman : countries)
            {

                if(continent.getList_countries().contains(countryhuman))
                {
                    armytrue++;
                }
            }
            if(armytrue == continent.getList_countries().size())
            {
                army += continent.getArmyBonus();
            }
        }
        return army + calculatearmyreinforcement();
    }



    @Override
    public String attack(Country countryclicked, Country enemy, List<Player> players)
    {
        String result = "";
        if(countryclicked.getArmyStrength() > 1 && !players.get(0).getCountries().contains(enemy)
                && !players.get(1).getCountries().contains(countryclicked))
        {
            int armycount = Math.min(countryclicked.getArmyStrength() - 1, 3);
            int[] attacker = players.get(0).toDice(armycount);
            int[] defender = players.get(1).toDice(Math.min(enemy.getArmyStrength(), 2));
            int loopcount = (attacker.length >= defender.length) ? defender.length : attacker.length;

            for(int i = loopcount - 1; i >= 0; i--)
            {

                if(attacker[i] > defender[i])
                {
                    if(enemy.getArmyStrength() - 1 == 0)
                    {
                        players.get(0).getCountries().add(enemy);
                        for(Territory territory : enemy.getTerritories())
                        {
                            territory.setColor(Color.BLUE);
                        }
                        for(Territory territory : countryclicked.getTerritories())
                        {
                            territory.setColor(Color.BLUE);
                        }
                        countryclicked.setArmyStrength(countryclicked.getArmyStrength() - 1);

                        players.get(1).getCountries().remove(enemy);
                        result = "You won! You conquered " + enemy.getName();
                        System.out.println("---> " + result + "\nCarry on or end the round.\n");
                    }
                    else
                    {
                        enemy.setArmyStrength(enemy.getArmyStrength() - 1);
                        result = "Assault was a success.";
                    }
                }
                else if(attacker[i] <= defender[i])
                {
                    for(Territory territory : enemy.getTerritories())
                    {
                        territory.setColor(Color.RED);
                    }
                    for(Territory territory : countryclicked.getTerritories())
                    {
                        territory.setColor(Color.BLUE);
                    }
                    result = "Assault failed.";
                    System.out.println(result);
                    countryclicked.setArmyStrength(countryclicked.getArmyStrength() - 1);
                }
            }
            return result;
        }
        else if(players.get(0).getCountries().contains(enemy))
        {
            for(Territory territory : enemy.getTerritories())
            {
                territory.setColor(Color.BLUE);
            }
            for(Territory territory : countryclicked.getTerritories())
            {
                territory.setColor(Color.BLUE);
            }
        }
        return result;
    }



    @Override
    public int[] toDice(int countArmy)
    {
        int[] dices = new int[countArmy];
        for(int i = 0; i < dices.length; i++)
        {
            dices[i] = (int) (Math.random() * 6 + 1);
        }
        Arrays.sort(dices);

        return dices;
    }



    @Override
    public String moveArmy(Country c1, Country c2)
    {
        String result = "";

        if(c1.getArmyStrength() - 1 >= 1)
        {
            c1.setArmyStrength(c1.getArmyStrength() - 1);
            c2.setArmyStrength(c2.getArmyStrength() + 1);
            result = "You moved one Army from " + c1.getName() + " to " + c2.getName() + ".";
        }
        return result;
    }



    @Override
    public List<Country> getCountries()
    {
        return countries;
    }



    @Override
    public Phases won(List<Player> players, List<Country> listofcountry)
    {

        Phases result = Phases.NO_CHANGE;

        if(this.getCountries().size() == 0)
        {
            result = Phases.LOST;
        }
        if(this.getCountries().size() == listofcountry.size())
        {
            result = Phases.WON;
        }
        return result;
    }
}
