package Catalog;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SearchEngine {

    private Catalog catalog;
    private Integer[] array;
    private List<Composition> answerList;

    public SearchEngine(Catalog catalog) {
        this.catalog = catalog;
        answerList = catalog.getCompositionList();
        array = new Integer[]{0, 0, 0, 0, 0};
    }

    public List<Composition> getAnswerList() {
        return answerList;
    }

    public Integer[] requestParsing(String request) throws IOException {
        BufferedReader file;
        try {
            file = new BufferedReader(new FileReader(catalog.getCacheFile()));
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
            for (int i = 0; i < 4; i++) {
                if (args[i].equals(request)) {
                    array[i]++;
                }
            }
        }
        for (Genre genre : catalog.getAllGenres()) {
            if (request.equals(genre.getGenreName()))
                array[4]++;
        }
        file.close();
        return array;
    }

    public void setCompositionAnswer(Integer[] array, String request, List<Composition> compositionList) {
        List<Composition> tmpList = new ArrayList<Composition>();
        for (int i = 0; i < array.length; i++) {
            if (array[i] > 0) {
                final int requestResultSong = 0;
                final int requestResultAlbum = 1;
                final int requestResultYearOfAlbum = 2;
                final int requestResultArtist = 3;
                final int requestResultGenre = 4;
                switch (i) {
                    case requestResultGenre:
                        for (Genre genre : catalog.getAllGenres()) {
                            if (request.equals(genre.getGenreName())) {
                                if (genre.sonGenreListIsNotNull()) {
                                    for (Genre checkableGenre : genre.getGenreTreeBelow()) {
                                        for (Composition composition : compositionList) {
                                            if (checkableGenre == composition.getSong().getGenre()) {
                                                tmpList.add(composition);
                                            }
                                        }
                                    }
                                } else for (Composition composition : compositionList) {
                                    if (genre == composition.getSong().getGenre()) {
                                        tmpList.add(composition);
                                    }
                                }
                            }
                        }
                        break;
                    case requestResultSong:
                        for (Composition composition : compositionList) {
                            if (request.equals(composition.getSong().getSongName())) {
                                tmpList.add(composition);
                            }
                        }
                        break;
                    case requestResultAlbum:
                        for (Composition composition : compositionList) {
                            if (request.equals(composition.getAlbum().getAlbumName())) {
                                tmpList.add(composition);
                            }
                        }
                        break;
                    case requestResultYearOfAlbum:
                        for (Composition composition : compositionList) {
                            if (request.equals(composition.getAlbum().getYearOfAlbum())) {
                                tmpList.add(composition);
                            }
                        }
                        break;
                    case requestResultArtist:
                        for (Composition composition : compositionList) {
                            if (request.equals(composition.getArtist().getArtistName())) {
                                tmpList.add(composition);
                            }
                        }
                        break;
                }
            }
        }
        this.answerList = tmpList;
    }
}