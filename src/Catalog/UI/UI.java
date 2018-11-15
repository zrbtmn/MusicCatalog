package Catalog.UI;

import Catalog.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class UI {

    private Scanner in = new Scanner(System.in);
    private Catalog catalog;
    private SearchEngine searchEngine;

    public UI(Catalog catalog) {
        this.catalog = catalog;
    }

    public void menu() throws Exception {
        String type;
        printMenu();
        while (!(type = in.nextLine()).equals("exit")) {
            switch (type) {
                case "1":
                    printAllCompositions();
                    break;
                case "2":
                    printFile();
                    break;
                case "3":
                    writeNewComposition();
                    System.out.print("\nAdd one more? y/n\n >> ");
                    while (!(type = in.nextLine()).equals("n")) {
                        if (type.equals("y")) {
                            writeNewComposition();
                            catalog.readFile(catalog.getCacheFile());
                        }
                    }
                    break;
                case "4":
                    boolean firstTime = true;
                    while (!type.equals("n")) {
                        if (type.equals("y") || firstTime) {
                            searchEngine = new SearchEngine(catalog);
                            String[] requestList = getRequest();
                            for (String request : requestList) {
                                searchEngine.setCompositionAnswer(searchEngine.requestParsing(request), request, searchEngine.getAnswerList());
                            }
                            printAnswerList();
                            firstTime = false;
                            System.out.print("Search again? y/n\n >> ");
                            type = in.nextLine();
                        } else break;
                    }
                case "exit":
                    break;
                default:
                    printMenu();
                    continue;
            }
            System.out.print("\n*Type \"menu\" to get back to menu\n*Type \"exit\" to quit\n >> ");
            String answer;
            while (!(answer = in.nextLine()).equals("menu")) {
                if (answer.equals("exit"))
                    System.exit(0);
                System.out.print("\n*Type \"menu\" to get back to menu\n*Type \"exit\" to quit \n >> ");
            }
            printMenu();
        }
    }


    private void printMenu() {
        for (int i = 0; i < 50; ++i) System.out.println();
        System.out.print("_Music_Catalog_\n\nMenu: \n1) Show catalog\n2) Show cache file\n" +
                "3) Add new composition\n4) Search\n*Type number to choose\n*Type \"exit\" to quit\n >> ");
    }

    private void printAnswerList() {
        if (searchEngine.getAnswerList().size() != 0) {
            System.out.print("\nHere you are!\n");
            for (Composition composition : searchEngine.getAnswerList()) {
                composition.printComposition();
            }
        } else System.out.print("\nOoops\nNot found :(\n");
    }

    private void printAllCompositions() {
        for (Artist artist : catalog.getAllArtists()) {
            for (Album album : artist.getAlbumList()) {
                for (Song song : album.getSongList()) {
                    System.out.println(song.getMainGenreName() + "\t" + song.getGenreName() + "\t" + song.getSongName() + "\t"
                            + album.getAlbumName() + "\t" + artist.getArtistName());
                }
            }
        }
    }

    private void printFile() throws IOException {
        BufferedReader file;
        try {
            file = new BufferedReader(new FileReader(catalog.getCacheFile()));
        } catch (FileNotFoundException ex) {
            System.err.println("Cache file not found");
            throw ex;
        }
        int counter = 1;
        while (true) {
            String line;
            try {
                line = file.readLine();
            } catch (IOException ex) {
                System.err.println("READER ERROR");
                throw ex;
            }
            if (line == null) {
                break;
            }
            System.out.print(counter + ")\t");
            String[] args = line.split("______");
            for (String arg : args) {
                System.out.print(arg + "\t");
            }
            counter++;
            System.out.println();
        }
    }

    private void printGenres(Genre genre, int count) {
        System.out.print("\t|");
        for (int i = 0; i < count; i++)
            System.out.print(" ");
        System.out.println(genre.getGenreName());
        for (Genre subGenre : genre.getAllGenreSons()) {
            printGenres(subGenre, count + 4);
        }
    }

    private void writeNewComposition() throws Exception {
        String songName, albumName, albumYear, artistName;

        Scanner in = new Scanner(System.in);
        System.out.print("Let's set new composition!\n\n");
        System.out.print("Type song name:\t\t >> ");
        songName = in.nextLine();
        System.out.print("Type album name: \t >> ");
        albumName = in.nextLine();
        System.out.print("Type year:\t\t\t >> ");
        albumYear = in.nextLine();
        System.out.print("Type artist name:\t >> ");
        artistName = in.nextLine();

        while (true) {
            System.out.print("\nWould you like to do?\n\t1) Add a new genre\n\t2) Add subGenre\n\t3) Choose from the list\n\t4) Show genre tree\n" +
                    "\t*Type \"exit\" to quit\n >> ");
            String answer;
            answer = in.nextLine();
            switch (answer) {
                case "1":
                    System.out.print("Type main genre\t\t >> ");
                    answer = in.nextLine();
                    if (catalog.genreDoesNotExist(answer)) {
                        Genre parentGenre = new Genre(answer, null);
                        catalog.getAllGenres().add(parentGenre);
                        System.out.print("Type sub genre\t\t >> ");
                        answer = in.nextLine();
                        if (catalog.genreDoesNotExist(answer)) {
                            Genre genre = new Genre(answer, parentGenre);
                            parentGenre.setSonGenre(genre);
                            catalog.getAllGenres().add(genre);
                            genre = addSubGenre(in, answer, genre);
                            System.out.print(genre.getGenreName() + " ");
                            printGenres(genre, 0);
                            System.out.print("\nDone!\n");
                            continue;
                        } else {
                            throw new Exception("Genre exists already\n");
                        }
                    } else {
                        Genre parentGenre = catalog.findGenre(answer);
                        System.out.print("Type sub genre\t\t >> ");
                        answer = in.nextLine();
                        addNewSubGenreToGenre(in, answer, parentGenre);
                        continue;
                    }
                case "2":
                    System.out.print("Which genre you want to expand\n >> ");
                    answer = in.nextLine();
                    if (!catalog.genreDoesNotExist(answer)) {
                        Genre parentGenre = catalog.findGenre(answer);
                        System.out.print("Type sub genre\t\t >> ");
                        answer = in.nextLine();
                        addNewSubGenreToGenre(in, answer, parentGenre);
                        continue;
                    } else {
                        System.out.println("There is no genre with this name\n");
                        continue;
                    }
                case "3":
                    System.out.print("Type the name of genre to choose:\n >> ");
                    answer = in.nextLine();
                    if (!catalog.findGenre(answer).isMainGenre()) {
                        Song song = new Song(songName, catalog.findGenre(answer));
                        Composition composition = catalog.setComposition(artistName, albumName, albumYear, song);
                        catalog.getCompositionList().add(composition);
                        catalog.writeComposition(composition);
                        composition.printComposition();
                        System.out.print("Great!\n");
                    } else {
                        System.out.println("This genre is main OR genre doesn't exist\n");
                        continue;
                    }
                    break;
                case "4":
                    System.out.print("\n\t __________ \n\t|GENRE TREE|\n\t|__________\n\t|\n");
                    for (Genre genre : catalog.getAllGenres()) {
                        if (genre.isMainGenre()) {
                            printGenres(genre, 0);
                        }
                    }
                    System.out.print("\t|__________ \n");
                    continue;
                case "exit":
                    System.exit(0);
                default:
                    System.out.println("Do you want to exit? y/n\n >> ");
                    answer = in.nextLine();
                    switch (answer) {
                        case "y":
                            System.exit(0);
                        case "n":
                            continue;
                        default:
                            System.out.println("There is no option like the one you have typed. " +
                                    "\nGoodbye\n");
                            System.exit(0);
                    }
            }
            break;
        }
    }

    private void addNewSubGenreToGenre(Scanner in, String answer, Genre parentGenre) throws Exception {
        if (catalog.genreDoesNotExist(answer)) {
            Genre genre = new Genre(answer, parentGenre);
            catalog.getAllGenres().add(genre);
            parentGenre.setSonGenre(genre);
            genre = addSubGenre(in, answer, genre);
            System.out.print(genre.getGenreName() + " ");
            printGenres(genre, 0);
            System.out.print("\nDone!\n");
        } else {
            System.out.print("Genre exists already\n\n");
        }
    }

    private Genre addSubGenre(Scanner in, String answer, Genre genre) throws Exception {
        while (!answer.equals("n")) {
            System.out.print("Would you like to add sub genre? y/n\n >> ");
            answer = in.nextLine();
            if (answer.equals("y")) {
                System.out.print("Type sub genre\t\t >> ");
                answer = in.nextLine();
                if (catalog.genreDoesNotExist(answer)) {
                    genre = catalog.addGenre(answer, genre);
                    catalog.getAllGenres().add(genre);
                } else {
                    System.out.print("Genre exists already\n\n");
                    break;
                }
            }
        }
        return genre;
    }

    private String[] getRequest() {
        Scanner in = new Scanner(System.in);
        System.out.print("Search >> ");
        String request = in.nextLine();
        return request.split(" ");
    }
}