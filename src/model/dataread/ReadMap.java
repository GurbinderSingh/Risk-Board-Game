package model.dataread;

import model.logic.Continent;
import model.logic.Country;
import model.logic.Territory;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReadMap
{
    private String path;
    private List<Territory> list_territory;
    private List<Continent> list_continent;
    private List<Country> list_country;
    private String message;

    private Map<String, Country> map_country;



    public ReadMap(String path)
    {
        this.path = path;
        this.list_territory = new ArrayList<>();
        this.list_continent = new ArrayList<>();
        this.list_country = new ArrayList<>();
        this.map_country = new HashMap<>();
        this.message = "";
    }



    public List<Country> readfile()
    {

        Path file = Paths.get(this.path);
        try(InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            String line = null;
            String delimitInt = "(?:\\d*\\.)?\\d+";
            String delimiterWord = "([A-Z])\\w+";

            while((line = reader.readLine()) != null)
            {

                read_territory_capital(line, delimiterWord, delimitInt, "patch-of ");

                read_territory_capital(line, delimiterWord, delimitInt, "capital-of ");

                read_neighbors_continet(line, "neighbors-of ", delimiterWord, delimitInt);

                read_neighbors_continet(line, "continent ", delimiterWord, delimitInt);
            }
            for(String key : map_country.keySet())
            {
                this.list_country.add(map_country.get(key));
            }
        }
        catch(IOException x)
        {

            JOptionPane.showMessageDialog(null,
                    "It is not a Valid Path",
                    "Path Warning",
                    JOptionPane.WARNING_MESSAGE);
            System.err.println(x);
        }

        return this.list_country;
    }



    private void read_territory_capital(String line, String delimitWord, String delimitInt, String argument)
    {
        List<Integer> coordinates = new ArrayList<>();

        if(line.startsWith(argument))
        {

            String territoryname = "";
            String[] items = line.split(" ");
            for(int i = 0; i < items.length; i++)
            {
                if(items[i].matches(delimitWord))
                {
                    territoryname += items[i] + " ";
                }
                else if(items[i].matches(delimitInt))
                {
                    coordinates.add(Integer.parseInt(items[i]));
                }
            }
            territoryname = territoryname.replaceAll("\\s+$", "");

            if(coordinates.size() > 2)
            {
                Territory newterritory = new Territory(territoryname, coordinates);
                list_territory.add(newterritory);
            }
            else
            {
                map_country.put(territoryname, new Country(territoryname, coordinates.get(0), coordinates.get(1)));
                for(Territory territory : list_territory)
                {
                    if(map_country.containsKey(territory.getTerritoryname()))
                    {
                        Country country_temp = map_country.get(territory.getTerritoryname());
                        country_temp.getTerritories().add(territory);
                    }
                }
            }
        }
    }



    private void read_neighbors_continet(String line, String arguments, String delimitWord, String delimitInt)
    {
        if(line.startsWith(arguments))

        {
            String name = "";
            int armyBonus = 0;

            String[] items = line.split(" : ");
            String[] itemState = items[0].split(" ");
            String[] itemNeighbors = items[1].split(" - ");
            for(int i = 0; i < itemState.length; i++)
            {
                if(itemState[i].matches(delimitWord))
                {
                    name += itemState[i] + " ";
                }
                else if(itemState[i].matches(delimitInt))
                {
                    armyBonus = Integer.parseInt(itemState[i]);
                }
            }
            name = name.replaceAll("\\s+$", "");
            Continent continent_temp = new Continent(name, armyBonus);
            Country country_temp = map_country.get(name);


            for(int listN = 0; listN < itemNeighbors.length; listN++)
            {

                if(armyBonus > 0)
                {
                    continent_temp.getList_countries().add(map_country.get(itemNeighbors[listN]));
                }
                else
                {
                    country_temp.getNeighbors().add(map_country.get(itemNeighbors[listN]));
                }
            }

            if(armyBonus > 0)
            {
                list_continent.add(continent_temp);
            }
            else
            {
                for(Territory territory : list_territory)
                {

                    territory.setCapital_X(map_country.get(territory.getTerritoryname()).getCapital_X());
                    territory.setCapital_Y(map_country.get(territory.getTerritoryname()).getCapital_Y());
                    territory.getNeigbors().addAll(map_country.get(territory.getTerritoryname()).getNeighbors());
                }
            }
        }
    }



    public List<Territory> getList_territory()
    {
        return list_territory;
    }



    public List<Continent> getList_continent()
    {
        return list_continent;
    }



    public void setMessage(String text)
    {
        message = text;
    }



    public void drawMessage(Graphics2D g2)
    {
        g2.setFont(new Font("default", Font.BOLD, 14));

        g2.drawString(message, 400, 595);
    }
}
