package model.logic.gamer;

import model.logic.Continent;
import model.logic.Country;
import model.logic.Phases;

import java.util.List;


public interface Player
{

    int calculatearmyreinforcement();

    int hasContinent(List<Continent> continents);

    String attack(Country countryclicked, Country enemy, List<Player> players);

    int[] toDice(int countArmy);

    String moveArmy(Country c1, Country c2);

    List<Country> getCountries();

    Phases won(List<Player> players, List<Country> listofcountry);
}
