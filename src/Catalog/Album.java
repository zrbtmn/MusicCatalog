package Catalog;

import java.util.ArrayList;
import java.util.List;

public class Album {

    private String albumName;
    private String yearOfAlbum;
    private List<Song> songList = new ArrayList<>();

    Album(String albumName, String yearOfAlbum){
        this.albumName = albumName;
        this.yearOfAlbum = yearOfAlbum;
    }

    void addSongToAlbum(Song song){
        if (!doesItExist(song))
            songList.add(song);
    }

    boolean equalsTo(Album album){
        return this.albumName.equals(album.getAlbumName());
    }

    String getYearOfAlbum() {
        return yearOfAlbum;
    }

    public String getAlbumName(){
        return albumName;
    }

    public List<Song> getSongList(){
        return songList;
    }


    private boolean doesItExist(Song checkSong){
        for (Song song: songList) {
            if (song.equalsTo(checkSong))
                return true;
        }
        return false;
    }
}