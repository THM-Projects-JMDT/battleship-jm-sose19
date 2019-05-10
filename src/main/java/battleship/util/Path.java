package battleship.util;

public class Path {
    public class Web {
        public static final String GAME = "/game";
        public static final String PLAYER = "/player";
        public static final String GETPAGE = "/getside";
        public static final String NEWGAME = GAME + "/new";
        public static final String JOINGAME = GAME + "/join";
        public static final String ABOUTGAME = GAME + "/about";
        public static final String SETNAME = PLAYER + "/setname";
        public static final String SSE = "/sse";
    }

    public class Pages {
        private static final String DIR = "pages/";     
        private static final String END = ".html"; 
        public static final String START = DIR + "start" + END;
        public static final String LOGIN = DIR + "login" + END;
        public static final String GAME = DIR + "game" + END;
    }
}