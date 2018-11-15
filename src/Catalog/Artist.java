package Catalog;

import java.util.ArrayList;
import java.util.List;

public class Artist {

    private String artistName;
    private List<Album> albumList = new ArrayList<>();

    Artist(String artistName){
        this.artistName = artistName;
    }

    void addAlbumToArtist(Album album) {
        if (!albumExists(album))
            albumList.add(album);
    }

    boolean doesNotExist(String albunName){
        for (Album searchableAlbum: albumList) {
            if (albunName.equals(searchableAlbum.getAlbumName()))
                return false;
        }
        return true;
    }

    public String getArtistName() {
        return artistName;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }


    private boolean albumExists(Album checkAlbum){
        for (Album album: albumList) {
            if (album.equalsTo(checkAlbum))
                return true;
        }
        return false;
    }
}