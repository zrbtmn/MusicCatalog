package Catalog;

public class Composition {

    private Artist artist;
    private Album album;
    private Song song;

    Composition(Artist artist, Album album, Song song){
        this.artist = artist;
        this.album = album;
        this.song = song;
    }

    public void printComposition(){
        System.out.println(song.getMainGenreName() + "\t" + song.getGenreName() + "\t" + song.getSongName() + "\t"
                + album.getAlbumName() + "\t" + artist.getArtistName());
    }

    Artist getArtist() {
        return artist;
    }

    Album getAlbum() {
        return album;
    }

    Song getSong(){
        return song;
    }
}