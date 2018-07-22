package controller;

import model.dataread.ReadMap;
import model.logic.Continent;
import model.logic.Country;
import model.logic.Phases;
import model.logic.Territory;
import model.logic.gamer.Computer;
import model.logic.gamer.Human;
import model.logic.gamer.Player;
import view.gui.MapGUI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


public class LogicController
{

    private List<Player> players;
    private int armystrength;
    private int pc_temp_army;
    private List<Country> leftlist;
    private List<Country> rightlist;



    public LogicController()
    {
        players = new ArrayList<>();
        players.add(new Human());
        players.add(new Computer());
        armystrength = 0;
        pc_temp_army = 0;
        leftlist = new ArrayList<>();
        rightlist = new ArrayList<>();
    }



    public Phases claimfunction(List<Continent> continents, List<Country> listofcountry,
                                List<Territory> listofterrirtory, MapGUI mapGUI, MouseEvent mouseEvent)
    {
        Phases finish = isAllTaken(listofcountry);
        if(finish == Phases.PHASE_ONE)
        {

            if(mouseEvent.getButton() == mouseEvent.BUTTON1)
            {
                Point mousepoint = mouseEvent.getPoint();

                for(Territory territory : listofterrirtory)
                {
                    if(territory.getPolygon().contains(mousepoint))
                    {

                        for(Country country : listofcountry)
                        {
                            if(!country.isTaken() && country.getTerritories().contains(territory))
                            {
                                mapGUI.getStatus().setText("You Claimed: " + territory.getTerritoryname());
                                for(Territory irland : country.getTerritories())
                                {
                                    irland.setColor(Color.blue);
                                    irland.setIsTaken(true);
                                    country.setTaken(true);
                                }
                                Human human = (Human) players.get(0);
                                Computer computer = (Computer) players.get(1);

                                human.claim(country);
                                mapGUI.getStatus().setText(computer.claim(listofcountry, isAllTaken(listofcountry)));
                                listofcountry = computer.returnAvailabellist();
                            }
                        }
                    }
                }
            }
        }
        if(isAllTaken(listofcountry) == Phases.PHASE_TWO)
        {


            for(Country country : listofcountry)
            {
                country.setArmyStrength(1);
            }
            for(Player pl : players)
            {
                pc_temp_army = pl.hasContinent(continents);
            }
            armystrength = players.get(0).hasContinent(continents);
            mapGUI.getStatus().setText("You have now " + armystrength + " Army Reinforcement");
        }

        mapGUI.repaint();
        return finish;
    }



    public Phases isAllTaken(List<Country> listforController)
    {

        for(Country country : listforController)
        {
            if(!country.isTaken())
            {
                return Phases.PHASE_ONE;
            }
        }
        return Phases.PHASE_TWO;
    }



    public Phases setArmyForcement(ReadMap readMap, List<Territory> listofterrirtory, MapGUI mapGUI, MouseEvent mouseEvent)
    {
        readMap.setMessage("");

        if(mouseEvent.getButton() == mouseEvent.BUTTON1)
        {

            int temp_reinforcment = 1;
            if(armystrength != 0)
            {
                Point mousepoint = mouseEvent.getPoint();

                for(Territory territory : listofterrirtory)
                {
                    if(territory.getPolygon().contains(mousepoint))
                    {
                        for(Country country : players.get(0).getCountries())
                        {
                            if(country.getTerritories().contains(territory))
                            {
                                temp_reinforcment += country.getArmyStrength();
                                country.setArmyStrength(temp_reinforcment);

                                armystrength--;

                                mapGUI.getStatus().setText("You have now " + armystrength + " Army Reinforcement");
                            }
                        }
                    }
                }
            }
        }
        mapGUI.repaint();
        return (armystrength == 0) ? setArmyComp() : Phases.PHASE_TWO;
    }



    public Phases setArmyComp()
    {
        int value = 0;
        Phases resut = Phases.PHASE_TWO;
        if(armystrength == 0)
        {
            Computer cp = (Computer) players.get(1);

            value = cp.setReinforcment(players.get(0));
            resut = (value != 0) ? Phases.PHASE_TWO : Phases.PHASE_THREE;
        }
        return resut;
    }



    public String moveandattack(ReadMap readMap, List<Country> listofcountry, List<Territory> listofterrirtory, MapGUI mapGUI, MouseEvent mouseEvent)
    {
        String result = "";

        if(mouseEvent.getButton() == mouseEvent.BUTTON3)
        {

            Point mousepoint = mouseEvent.getPoint();
            for(Territory territory : listofterrirtory)
            {
                if(territory.getPolygon().contains(mousepoint))
                {
                    for(Country country : players.get(0).getCountries())
                    {
                        if(country.getTerritories().contains(territory))
                        {
                            rightlist.add(country);
                        }
                    }
                }
            }
            if(rightlist.size() == 2)
            {
                Country c1 = rightlist.get(0);
                Country c2 = rightlist.get(1);
                if(c2.getNeighbors().contains(c1) && c1 != c2)
                {

                    result = players.get(0).moveArmy(c1, c2);
                }
                else if(c1.getNeighbors().contains(c2) && c2 != c1)
                {

                    result = players.get(0).moveArmy(c1, c2);
                }
                rightlist.clear();
            }
        }
        else if(mouseEvent.getButton() == mouseEvent.BUTTON1)
        {
            Color color = Color.BLUE;
            boolean temp = true;
            Point mousepoint = mouseEvent.getPoint();
            for(Territory territory : listofterrirtory)
            {

                if(territory.getPolygon().contains(mousepoint))
                {

                    for(Country country : listofcountry)
                    {

                        if(country.getTerritories().contains(territory))
                        {

                            if(leftlist.size() <= 0 && players.get(0).getCountries().contains(country) && country.getArmyStrength() > 1 && temp)
                            {

                                color = new Color(30, 144, 255);
                                for(Territory territory1 : country.getTerritories())
                                {

                                    territory1.setColor(color);
                                }
                                leftlist.add(country);
                                temp = false;
                                result = "You selected: " + country.getName();
                            }
                            else if((leftlist.size() >= 1 && players.get(1).getCountries().contains(country)) &&
                                    (leftlist.get(0).getNeighbors().contains(country) || country.getNeighbors().contains(leftlist.get(0))))
                            {

                                color = new Color(255, 69, 0);
                                for(Territory territory1 : country.getTerritories())
                                {

                                    territory1.setColor(color);
                                }
                                leftlist.add(country);
                                result += " You selected as Enemy's Country " + country.getName();
                            }
                            else
                            {

                                for(Country tmp : leftlist)
                                {

                                    if(players.get(0).getCountries().contains(tmp))
                                    {

                                        for(Territory territory1 : country.getTerritories())
                                        {

                                            territory1.setColor(Color.blue);
                                        }
                                    }
                                    else
                                    {

                                        for(Territory territory1 : country.getTerritories())
                                        {

                                            territory1.setColor(Color.red);
                                        }
                                    }
                                }
                                leftlist.clear();
                            }
                        }
                    }
                }
            }
            if(leftlist.size() == 2)
            {

                Country c1 = leftlist.get(0);
                Country c2 = leftlist.get(1);
                if(c2.getNeighbors().contains(c1) && c1 != c2)
                {

                    result = players.get(0).attack(c1, c2, players);
                }
                else if(c1.getNeighbors().contains(c2) && c2 != c1)
                {

                    result = players.get(0).attack(c1, c2, players);
                }
                leftlist.clear();
                temp = true;
            }
        }
        mapGUI.repaint();
        readMap.setMessage(result);
        return result;
    }



    public boolean attackComputer(ReadMap readMap, int getComputer)
    {
        System.out.println("\n===== Computer attacking now. =====");

        Computer computer = (Computer) players.get(getComputer);
        Country[] selected = computer.chooseMyAttackingCountry(players);
        Country attacking = selected[0];
        Country toattack = selected[1];

        readMap.setMessage("Computer attacking " + toattack.getName() + ", with " + attacking.getName());

        readMap.setMessage(players.get(getComputer).attack(attacking, toattack, players) + " Your turn.");


        return true;
    }



    public List<Player> getPlayers()
    {
        return players;
    }



    public void calculateReinformcement(List<Continent> continents, MapGUI mapGUI)
    {
        for(Player pl : players)
        {
            pc_temp_army = pl.hasContinent(continents);
        }
        armystrength = players.get(0).hasContinent(continents);
        mapGUI.getStatus().setText("You have now " + armystrength + " Army Reinforcement");
    }



    public Phases won(Player players, List<Country> listofcountry)
    {
        return players.won(this.getPlayers(), listofcountry);
    }
}
