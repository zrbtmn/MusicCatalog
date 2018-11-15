package Catalog;

public class Song {

    private String songName;
    private Genre genre;

    public Song(String songName, Genre genre){
        this.songName = songName; this.genre = genre;
    }

    public String getSongName(){
        return songName;
    }

    public String getGenreName() {return genre.getGenreName(); }

    public String getMainGenreName() {
        Genre mainGenre = genre;
        while (!mainGenre.isMainGenre()){
            mainGenre = mainGenre.getParentGenre();
        }
        return mainGenre.getGenreName();
    }

    boolean equalsTo(Song song){
        return this.songName.equals(song.getSongName());
    }

    Genre getGenre() {
        return genre;
    }
}