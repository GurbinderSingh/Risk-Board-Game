package model.logic.gamer;

import model.logic.Continent;
import model.logic.Country;
import model.logic.Phases;
import model.logic.Territory;

import java.awt.*;
import java.util.*;
import java.util.List;


public class Computer implements Player
{
    private int armies;

    private List<Country> listofcomputer;
    private List<Country> listforController;



    public Computer()
    {
        armies = 0;
        listofcomputer = new ArrayList<>();
        listforController = new ArrayList<>();
    }



    public String claim(List<Country> country_parts, Phases isAllTaken)
    {
        String result = "";
        boolean changed = true;
        while(changed)
        {
            int random = (int) (Math.random() * (country_parts.size() - 1));
            Country countrytemp = null;
            for(Country country : country_parts)
            {
                if(country.getName().equals("Indonesia") && !country.isTaken())
                {
                    countrytemp = country;
                }
                else
                {
                    countrytemp = country_parts.get(random);
                }
            }

            if(!country_parts.get(random).isTaken())
            {
                for(Territory territory : countrytemp.getTerritories())
                {

                    territory.setColor(Color.RED);
                    territory.setIsTaken(true);
                    countrytemp.setTaken(true);
                }
                result += "Computer claimed " + countrytemp.getName();
                listofcomputer.add(countrytemp);
                listforController = country_parts;

                changed = false;
            }
            else if(isAllTaken == Phases.PHASE_TWO)
            {
                changed = false;
            }
        }
        return result;
    }



    public List<Country> returnAvailabellist()
    {
        return this.listforController;
    }



    @Override
    public int calculatearmyreinforcement()
    {
        int value = (listofcomputer.size() / 3);

        return value;
    }



    @Override
    public int hasContinent(List<Continent> continents)
    {
        int army = 0;
        for(Continent continent : continents)
        {
            int armytrue = 0;
            for(Country countrycomputer : listofcomputer)
            {

                if(continent.getList_countries().contains(countrycomputer))
                {
                    armytrue++;
                }
            }
            if(armytrue == continent.getList_countries().size())
            {

                army += continent.getArmyBonus();
            }
        }
        return armies = army + calculatearmyreinforcement();
    }



    public Country[] chooseMyAttackingCountry(List<Player> players)
    {
        Player pcPlayer = players.get(1);
        Country[] battlingCountries = new Country[]{null, null};

        for(Country pcCountry : pcPlayer.getCountries())
        {
            if(pcCountry.getArmyStrength() > 1 &&
                    (battlingCountries[0] == null || pcCountry.getArmyStrength() > battlingCountries[0].getArmyStrength()))
            {

                battlingCountries[1] = chooseWhichCountryToAttack(players.get(0), pcCountry);

                if(battlingCountries[1] != null)
                {
                    battlingCountries[0] = pcCountry;
                }
            }
        }


        if(battlingCountries[1] != null)

        {
            for(Territory territory : battlingCountries[0].getTerritories())
            {
                territory.setColor(new Color(255, 150, 50));
            }
        }

        return battlingCountries;
    }



    public Country chooseWhichCountryToAttack(Player humanPlayer, Country myAttackingCountry)
    {
        List<Country> myNeighboringCountries = myAttackingCountry.getNeighbors();
        Country attackThisCountry = null;
        boolean hasEnemyNeighbor = false;

        for(Country neighbor : myNeighboringCountries)  //schau ob mi. ien nachbar = gegner ist.
        {
            if(humanPlayer.getCountries().contains(neighbor))
            {
                hasEnemyNeighbor = true;
            }
        }
        if(!hasEnemyNeighbor)   //falls nicht, schau welche gegneri. länder mich als nachbar enthalten.
        {

            myNeighboringCountries = new ArrayList<>();

            for(Country tmp : humanPlayer.getCountries())
            {

                if(tmp.getNeighbors().contains(myAttackingCountry))
                {

                    myNeighboringCountries.add(tmp);
                }
            }
        }
        if(myNeighboringCountries.size() == 0) return null; //falls keins, gib null zurück


        for(Country neighboringCountry : myNeighboringCountries)    //gehe alle Länder durch
        {

            if(humanPlayer.getCountries().contains(neighboringCountry))
            {

                if(attackThisCountry == null || neighboringCountry.getArmyStrength() < attackThisCountry.getArmyStrength())
                {

                    attackThisCountry = neighboringCountry;
                }
            }
        }
        for(Territory territory : attackThisCountry.getTerritories())
        {

            territory.setColor(new Color(0, 150, 180));
        }
        return attackThisCountry;
    }



    @Override
    public String attack(Country myAttackingCountry, Country enemyCountry, List<Player> players)
    {
        String result = "";

        if(enemyCountry != null || myAttackingCountry != null)
        {
            result = "Computer skipping round.";
            System.out.println("\n" + result + "\n");
        }

        while(enemyCountry != null && myAttackingCountry != null)
        {

            boolean check = false;
            System.out.println("\n-> Computer attacking with: " + myAttackingCountry.getName() + "\n-> Attacking here: " + enemyCountry.getName());


            while(myAttackingCountry.getArmyStrength() > 1 && !listofcomputer.contains(enemyCountry))
            {

                int myAttackingArmies = Math.min(myAttackingCountry.getArmyStrength() - 1, 3);
                int[] attacker = players.get(1).toDice(myAttackingArmies);
                int[] defender = players.get(0).toDice(Math.min(enemyCountry.getArmyStrength(), 2));
                int loopcount = (attacker.length >= defender.length) ? defender.length : attacker.length;

                for(int i = loopcount - 1; i >= 0; i--)
                {

                    if(attacker[i] > defender[i])
                    {
                        if(enemyCountry.getArmyStrength() - 1 == 0)
                        {
                            players.get(1).getCountries().add(enemyCountry);
                            for(Territory territory : enemyCountry.getTerritories())
                            {
                                territory.setColor(Color.RED);
                            }
                            for(Territory territory : myAttackingCountry.getTerritories())
                            {
                                territory.setColor(Color.RED);
                            }
                            myAttackingCountry.setArmyStrength(myAttackingCountry.getArmyStrength() - 1);
                            players.get(0).getCountries().remove(enemyCountry);

                            result = "Computer won! It conquered: " + enemyCountry.getName();
                            System.out.println("---> " + result + "\n");
                            check = true;
                        }
                        else
                        {
                            enemyCountry.setArmyStrength(enemyCountry.getArmyStrength() - 1);
                            result = "Assault was a success.";
                        }
                    }
                    else if(attacker[i] <= defender[i])
                    {
                        for(Territory territory : enemyCountry.getTerritories())
                        {
                            territory.setColor(Color.BLUE);
                        }
                        for(Territory territory : myAttackingCountry.getTerritories())
                        {
                            territory.setColor(Color.RED);
                        }
                        result = "Assault failed.";
                        System.out.println(result);
                        myAttackingCountry.setArmyStrength(myAttackingCountry.getArmyStrength() - 1);
                        check = true;
                    }
                }
                if(players.get(1).getCountries().contains(enemyCountry))
                {
                    for(Territory territory : enemyCountry.getTerritories())
                    {
                        territory.setColor(Color.RED);
                    }
                    for(Territory territory : myAttackingCountry.getTerritories())
                    {
                        territory.setColor(Color.RED);
                    }
                }
            }
            if(check)
            {
                Country[] tmp = chooseMyAttackingCountry(players);
                myAttackingCountry = tmp[0];
                enemyCountry = tmp[1];
            }
        }

        System.out.println("\nYour turn again.");
        System.out.println("\n===== Player attacking now. =====");
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
        return "";
    }



    @Override
    public List<Country> getCountries()
    {
        return listofcomputer;
    }



    public int setReinforcment(Player humanPlayer)
    {

        Map<Integer, Country> reinforcements = new HashMap<>();

        for(Country myCountry : this.getCountries())
        {
            int enemyCount = 0;
            for(Country enemyCountry : humanPlayer.getCountries())
            {
                if((enemyCountry.getNeighbors().contains(myCountry) || myCountry.getNeighbors().contains(enemyCountry))
                        && enemyCountry.getArmyStrength() > 1)
                    enemyCount++;
            }
            if(enemyCount >= 1) reinforcements.put(enemyCount, myCountry);
        }
        if(reinforcements.size() > 0)
        {
            for(Map.Entry<Integer, Country> entry : reinforcements.entrySet())
            {
                Country myCountry = entry.getValue();
                int enemies = entry.getKey();


                if(armies - enemies > 0)
                {
                    myCountry.setArmyStrength(enemies + myCountry.getArmyStrength());
                    armies -= enemies;
                }
                else
                {
                    myCountry.setArmyStrength(myCountry.getArmyStrength() + armies);
                    armies = 0;
                    break;
                }
            }
        }
        else
        {
            while(armies > 0)
            {
                int armyForCountry = (int) (Math.random() * armies + 1);
                int countryAtThisIndex = (int) (Math.random() * getCountries().size());
                Country thisCountry = getCountries().get(countryAtThisIndex);

                if(armies - armyForCountry >= 0)
                {
                    thisCountry.setArmyStrength(thisCountry.getArmyStrength() + armyForCountry);
                    armies -= armyForCountry;
                }
                else
                {
                    thisCountry.setArmyStrength(thisCountry.getArmyStrength() + armies);
                    armies = 0;
                }
            }
        }

        return armies;
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
