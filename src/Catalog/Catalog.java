package Catalog;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Catalog {

    private File cache;
    private List<Genre> allGenres = new ArrayList<Genre>();
    private List<Artist> allArtists = new ArrayList<Artist>();
    private List<Composition> compositionList = new ArrayList<Composition>();

    Catalog(File cache) throws Exception {
        this.cache = cache;
        readFile(cache);
    }

    public void readFile(File cache) throws Exception {
        BufferedReader file;
        try {
            file = new BufferedReader(new FileReader(cache));
        } catch (FileNotFoundException ex) {
            System.err.println("Cache file not found");
            throw ex;
        }
        while (true) {
            String line;
            try {
                line = file.readLine();
            } catch (IOException ex) {
                System.err.println("READER ERROR");
                throw ex;
            }
            if (line == null)
                break;

            String[] args = line.split("______");
            String songName = args[0];
            String albumName = args[1];
            String albumYear = args[2];
            String artistName = args[3];
            String[] genres = new String[args.length - 4];
            System.arraycopy(args, 4, genres, 0, args.length - 4);

            Song song = setSong(songName, genres);
            Composition composition = setComposition(artistName, albumName, albumYear, song);
            compositionList.add(composition);
        }
        file.close();
    }

    public void writeComposition(Composition composition) {
        try (FileWriter fileWriter = new FileWriter(cache, true)) {
            String songName = composition.getSong().getSongName();
            String albumName = composition.getAlbum().getAlbumName();
            String albumYear = composition.getAlbum().getYearOfAlbum();
            String artistName = composition.getArtist().getArtistName();
            String genres = getSongGenreTree(composition);
            String songInfo = "\n" + songName + "______" + albumName + "______" + albumYear
                    + "______" + artistName + genres;

            fileWriter.write(songInfo);
            fileWriter.flush();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public boolean genreDoesNotExist(String checkGenre) {
        for (Genre genre : allGenres) {
            if (checkGenre.equals(genre.getGenreName())) {
                return false;
            }
        }
        return true;
    }

    public File getCacheFile() {
        return cache;
    }

    public List<Genre> getAllGenres() {
        return allGenres;
    }

    public List<Artist> getAllArtists(){
        return allArtists;
    }

    public List<Composition> getCompositionList() {
        return compositionList;
    }

    public Genre findGenre(String searchableGenreName) throws Exception {
        for (Genre searchableGenre : allGenres) {
            if (searchableGenreName.equals(searchableGenre.getGenreName()))
                return searchableGenre;
        }
        throw new Exception("Couldn't find searchable genre.");
    }

    public Genre addGenre(String genreName, Genre genre) throws Exception {
        if (genreDoesNotExist(genreName)) {
            Genre subGenre = new Genre(genreName, genre);
            genre.setSonGenre(subGenre);
            genre = subGenre;
            allGenres.add(genre);
        } else throw new Exception("Genre exists already\n");
        return genre;
    }

    public Composition setComposition(String artistName, String albumName, String albumYear, Song song) throws Exception {
        if (artistDoesNotExist(artistName)) {                               // Если Артист существует
            if (findArtist(artistName).doesNotExist(albumName)) {               // Если не существует Альбома
                Album album = new Album(albumName, albumYear);
                album.addSongToAlbum(song);
                for (Artist artist : allArtists) {
                    if (artistName.equals(artist.getArtistName())) {
                        artist.addAlbumToArtist(album);
                        return new Composition(artist, album, song);
                    }
                }
            } else {                                                            // Если Альбом существеут
                for (Artist artist : allArtists) {
                    if (artistName.equals(artist.getArtistName())) {
                        for (Album searchableAlbum : artist.getAlbumList()) {
                            if (albumName.equals(searchableAlbum.getAlbumName())) {
                                searchableAlbum.addSongToAlbum(song);
                                return new Composition(artist, searchableAlbum, song);
                            }
                        }
                    }
                }
            }
        } else {                                                          // Если не существует Артиста, то нет и Альбома
            Artist artist = new Artist(artistName);
            Album album = new Album(albumName, albumYear);
            album.addSongToAlbum(song);
            artist.addAlbumToArtist(album);
            allArtists.add(artist);
            return new Composition(artist, album, song);
        }
        throw new Exception("FATAL_ERROR__PIZNEC");
    }


    private void getGenreTree(Genre genre, StringBuilder sb) {
        if (genre.getParentGenre() != null)
            getGenreTree(genre.getParentGenre(), sb);
        sb.append("______");
        sb.append(genre.getGenreName());
    }

    private boolean artistDoesNotExist(String searchableArtistName) {
        for (Artist searchableArtist : allArtists) {
            if (searchableArtistName.equals(searchableArtist.getArtistName()))
                return true;
        }
        return false;
    }

    private String getSongGenreTree(Composition composition){
        Genre genre = composition.getSong().getGenre();
        StringBuilder sb = new StringBuilder();
        getGenreTree(genre, sb);
        return sb.toString();
    }

    private Song setSong(String songName, String[] genres) throws Exception {
        if (genreDoesNotExist(genres[0])) {
            Genre genre = new Genre(genres[0], null);
            allGenres.add(genre);
            genre = setGenreTree(genres, genre);
            return new Song(songName, genre);
        } else {
            Genre genre = findGenre(genres[0]);
            genre = setGenreTree(genres, genre);
            return new Song(songName, genre);
        }
    }

    private Artist findArtist(String searchableArtistName) throws Exception {
        for (Artist searchableArtist : allArtists) {
            if (searchableArtistName.equals(searchableArtist.getArtistName()))
                return searchableArtist;
        }
        throw new Exception("Couldn't find searchable artist.");
    }

    private Genre setGenreTree(String[] genres, Genre genre) throws Exception {
        for (int i = 1; i < genres.length; i++) {
            if (genreDoesNotExist(genres[i])) {
                Genre subGenre = new Genre(genres[i], genre);
                genre.setSonGenre(subGenre);
                genre = subGenre;
                allGenres.add(genre);
            } else {
                Genre subGenre = findGenre(genres[i]);
                genre.setSonGenre(subGenre);
                genre = subGenre;
            }
        }
        return genre;
    }
}