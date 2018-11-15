package Catalog;

import java.util.ArrayList;
import java.util.List;

public class Genre {

    private String genreName;
    private Genre parentGenre;
    private List<Genre> allGenreSons = new ArrayList<Genre>();
    private boolean isMainGenre;

    public Genre(String genreName, Genre parentGenre){
        this.genreName = genreName;
        this.parentGenre = parentGenre;
    }

    public void setSonGenre(Genre sonGenre) {
        if (doesNotSonGenreExist(sonGenre))
            allGenreSons.add(sonGenre);
    }

    public String getGenreName(){
        return genreName;
    }

    public List<Genre> getAllGenreSons(){
        return allGenreSons;
    }

    public boolean isMainGenre(){
        if (parentGenre == null)
            isMainGenre = true;
        return isMainGenre;
    }

    boolean sonGenreListIsNotNull(){
        return allGenreSons.size() != 0;
    }

    Genre getParentGenre() {
        return parentGenre;
    }

    List<Genre> getGenreTreeBelow(){
        List<Genre> genreTreeBelow = new ArrayList<Genre>();
        if (sonGenreListIsNotNull()) {
            for (Genre genre : allGenreSons) {
                genreTreeBelow.add(genre);
                genreTreeBelow.addAll(genre.getGenreTreeBelow());
            }
        }
        return genreTreeBelow;
    }


    private boolean doesNotSonGenreExist(Genre checkableGenre){
        for(Genre genre: allGenreSons){
            if (genre == checkableGenre)
                return false;
        }
        return true;
    }
}