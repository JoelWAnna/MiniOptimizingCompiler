// Output created by jacc on Tue Apr 30 12:44:43 PDT 2013

package mini;

import compiler.*;
import java.io.*;

class MiniParser extends Phase implements MiniTokens {
    private int yyss = 100;
    private int yytok;
    private int yysp = 0;
    private int[] yyst;
    protected int yyerrno = (-1);
    private Object[] yysv;
    private Object yyrv;

    public boolean parse() {
        int yyn = 0;
        yysp = 0;
        yyst = new int[yyss];
        yysv = new Object[yyss];
        yytok = (lexer.getToken()
                 );
    loop:
        for (;;) {
            switch (yyn) {
                case 0:
                    yyst[yysp] = 0;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 148:
                    switch (yytok) {
                        case INT:
                        case ENDINPUT:
                        case VOID:
                        case DOUBLE:
                        case BOOLEAN:
                            yyn = yyr3();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 1:
                    yyst[yysp] = 1;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 149:
                    switch (yytok) {
                        case ENDINPUT:
                            yyn = 296;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 2:
                    yyst[yysp] = 2;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 150:
                    switch (yytok) {
                        case BOOLEAN:
                            yyn = 5;
                            continue;
                        case DOUBLE:
                            yyn = 6;
                            continue;
                        case INT:
                            yyn = 7;
                            continue;
                        case VOID:
                            yyn = 8;
                            continue;
                        case ENDINPUT:
                            yyn = yyr1();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 3:
                    yyst[yysp] = 3;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 151:
                    switch (yytok) {
                        case INT:
                        case ENDINPUT:
                        case VOID:
                        case DOUBLE:
                        case BOOLEAN:
                            yyn = yyr2();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 4:
                    yyst[yysp] = 4;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 152:
                    switch (yytok) {
                        case IDENT:
                            yyn = 9;
                            continue;
                        case '[':
                            yyn = 10;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 5:
                    yyst[yysp] = 5;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 153:
                    switch (yytok) {
                        case IDENT:
                        case '[':
                            yyn = yyr12();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 6:
                    yyst[yysp] = 6;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 154:
                    switch (yytok) {
                        case IDENT:
                        case '[':
                            yyn = yyr13();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 7:
                    yyst[yysp] = 7;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 155:
                    switch (yytok) {
                        case IDENT:
                        case '[':
                            yyn = yyr11();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 8:
                    yyst[yysp] = 8;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 156:
                    switch (yytok) {
                        case IDENT:
                            yyn = 11;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 9:
                    yyst[yysp] = 9;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 157:
                    switch (yytok) {
                        case '(':
                            yyn = 12;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 10:
                    yyst[yysp] = 10;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 158:
                    switch (yytok) {
                        case ']':
                            yyn = 13;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 11:
                    yyst[yysp] = 11;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 159:
                    switch (yytok) {
                        case '(':
                            yyn = 14;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 12:
                    yyst[yysp] = 12;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 160:
                    switch (yytok) {
                        case BOOLEAN:
                            yyn = 5;
                            continue;
                        case DOUBLE:
                            yyn = 6;
                            continue;
                        case INT:
                            yyn = 7;
                            continue;
                        case ')':
                            yyn = yyr6();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 13:
                    yyst[yysp] = 13;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 161:
                    switch (yytok) {
                        case IDENT:
                        case '[':
                            yyn = yyr14();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 14:
                    yyst[yysp] = 14;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 162:
                    switch (yytok) {
                        case BOOLEAN:
                            yyn = 5;
                            continue;
                        case DOUBLE:
                            yyn = 6;
                            continue;
                        case INT:
                            yyn = 7;
                            continue;
                        case ')':
                            yyn = yyr6();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 15:
                    yyst[yysp] = 15;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 163:
                    switch (yytok) {
                        case ',':
                            yyn = 20;
                            continue;
                        case ')':
                            yyn = yyr9();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 16:
                    yyst[yysp] = 16;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 164:
                    switch (yytok) {
                        case ')':
                            yyn = 21;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 17:
                    yyst[yysp] = 17;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 165:
                    switch (yytok) {
                        case ')':
                            yyn = yyr7();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 18:
                    yyst[yysp] = 18;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 166:
                    switch (yytok) {
                        case '[':
                            yyn = 10;
                            continue;
                        case IDENT:
                            yyn = 22;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 19:
                    yyst[yysp] = 19;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 167:
                    switch (yytok) {
                        case ')':
                            yyn = 23;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 20:
                    yyst[yysp] = 20;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 168:
                    switch (yytok) {
                        case BOOLEAN:
                            yyn = 5;
                            continue;
                        case DOUBLE:
                            yyn = 6;
                            continue;
                        case INT:
                            yyn = 7;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 21:
                    yyst[yysp] = 21;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 169:
                    switch (yytok) {
                        case '{':
                            yyn = 25;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 22:
                    yyst[yysp] = 22;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 170:
                    switch (yytok) {
                        case ')':
                        case ',':
                            yyn = yyr10();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 23:
                    yyst[yysp] = 23;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 171:
                    switch (yytok) {
                        case '{':
                            yyn = 26;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 24:
                    yyst[yysp] = 24;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 172:
                    switch (yytok) {
                        case ')':
                            yyn = yyr8();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 25:
                    yyst[yysp] = 25;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 173:
                    yyn = yys25();
                    continue;

                case 26:
                    yyst[yysp] = 26;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 174:
                    yyn = yys26();
                    continue;

                case 27:
                    yyst[yysp] = 27;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 175:
                    yyn = yys27();
                    continue;

                case 28:
                    yyst[yysp] = 28;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 176:
                    yyn = yys28();
                    continue;

                case 29:
                    yyst[yysp] = 29;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 177:
                    yyn = yys29();
                    continue;

                case 30:
                    yyst[yysp] = 30;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 178:
                    yyn = yys30();
                    continue;

                case 31:
                    yyst[yysp] = 31;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 179:
                    switch (yytok) {
                        case '[':
                            yyn = 10;
                            continue;
                        case IDENT:
                            yyn = 71;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 32:
                    yyst[yysp] = 32;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 180:
                    switch (yytok) {
                        case ',':
                            yyn = 72;
                            continue;
                        case ';':
                            yyn = 73;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 33:
                    yyst[yysp] = 33;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 181:
                    switch (yytok) {
                        case ';':
                            yyn = 74;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 34:
                    yyst[yysp] = 34;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 182:
                    switch (yytok) {
                        case ';':
                            yyn = 75;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 35:
                    yyst[yysp] = 35;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 183:
                    yyn = yys35();
                    continue;

                case 36:
                    yyst[yysp] = 36;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 184:
                    switch (yytok) {
                        case '(':
                            yyn = 77;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 37:
                    yyst[yysp] = 37;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 185:
                    yyn = yys37();
                    continue;

                case 38:
                    yyst[yysp] = 38;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 186:
                    switch (yytok) {
                        case '(':
                            yyn = 81;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 39:
                    yyst[yysp] = 39;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 187:
                    yyn = yys39();
                    continue;

                case 40:
                    yyst[yysp] = 40;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 188:
                    switch (yytok) {
                        case BOOLEAN:
                            yyn = 5;
                            continue;
                        case DOUBLE:
                            yyn = 6;
                            continue;
                        case INT:
                            yyn = 7;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 41:
                    yyst[yysp] = 41;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 189:
                    yyn = yys41();
                    continue;

                case 42:
                    yyst[yysp] = 42;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 190:
                    yyn = yys42();
                    continue;

                case 43:
                    yyst[yysp] = 43;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 191:
                    switch (yytok) {
                        case '(':
                            yyn = 81;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 44:
                    yyst[yysp] = 44;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 192:
                    yyn = yys44();
                    continue;

                case 45:
                    yyst[yysp] = 45;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 193:
                    yyn = yys45();
                    continue;

                case 46:
                    yyst[yysp] = 46;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 194:
                    yyn = yys46();
                    continue;

                case 47:
                    yyst[yysp] = 47;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 195:
                    yyn = yys47();
                    continue;

                case 48:
                    yyst[yysp] = 48;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 196:
                    yyn = yys48();
                    continue;

                case 49:
                    yyst[yysp] = 49;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 197:
                    yyn = yys49();
                    continue;

                case 50:
                    yyst[yysp] = 50;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 198:
                    switch (yytok) {
                        case INT:
                        case ENDINPUT:
                        case VOID:
                        case DOUBLE:
                        case BOOLEAN:
                            yyn = yyr4();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 51:
                    yyst[yysp] = 51;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 199:
                    yyn = yys51();
                    continue;

                case 52:
                    yyst[yysp] = 52;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 200:
                    switch (yytok) {
                        case INT:
                        case ENDINPUT:
                        case VOID:
                        case DOUBLE:
                        case BOOLEAN:
                            yyn = yyr5();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 53:
                    yyst[yysp] = 53;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 201:
                    yyn = yys53();
                    continue;

                case 54:
                    yyst[yysp] = 54;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 202:
                    yyn = yys54();
                    continue;

                case 55:
                    yyst[yysp] = 55;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 203:
                    yyn = yys55();
                    continue;

                case 56:
                    yyst[yysp] = 56;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 204:
                    yyn = yys56();
                    continue;

                case 57:
                    yyst[yysp] = 57;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 205:
                    yyn = yys57();
                    continue;

                case 58:
                    yyst[yysp] = 58;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 206:
                    yyn = yys58();
                    continue;

                case 59:
                    yyst[yysp] = 59;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 207:
                    yyn = yys59();
                    continue;

                case 60:
                    yyst[yysp] = 60;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 208:
                    yyn = yys60();
                    continue;

                case 61:
                    yyst[yysp] = 61;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 209:
                    yyn = yys61();
                    continue;

                case 62:
                    yyst[yysp] = 62;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 210:
                    yyn = yys62();
                    continue;

                case 63:
                    yyst[yysp] = 63;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 211:
                    yyn = yys63();
                    continue;

                case 64:
                    yyst[yysp] = 64;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 212:
                    yyn = yys64();
                    continue;

                case 65:
                    yyst[yysp] = 65;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 213:
                    yyn = yys65();
                    continue;

                case 66:
                    yyst[yysp] = 66;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 214:
                    yyn = yys66();
                    continue;

                case 67:
                    yyst[yysp] = 67;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 215:
                    yyn = yys67();
                    continue;

                case 68:
                    yyst[yysp] = 68;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 216:
                    yyn = yys68();
                    continue;

                case 69:
                    yyst[yysp] = 69;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 217:
                    yyn = yys69();
                    continue;

                case 70:
                    yyst[yysp] = 70;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 218:
                    switch (yytok) {
                        case ';':
                        case ',':
                            yyn = yyr27();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 71:
                    yyst[yysp] = 71;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 219:
                    switch (yytok) {
                        case '=':
                            yyn = 109;
                            continue;
                        case ';':
                        case ',':
                            yyn = yyr30();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 72:
                    yyst[yysp] = 72;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 220:
                    switch (yytok) {
                        case IDENT:
                            yyn = 71;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 73:
                    yyst[yysp] = 73;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 221:
                    yyn = yys73();
                    continue;

                case 74:
                    yyst[yysp] = 74;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 222:
                    yyn = yys74();
                    continue;

                case 75:
                    yyst[yysp] = 75;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 223:
                    yyn = yys75();
                    continue;

                case 76:
                    yyst[yysp] = 76;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 224:
                    switch (yytok) {
                        case WHILE:
                            yyn = 111;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 77:
                    yyst[yysp] = 77;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 225:
                    yyn = yys77();
                    continue;

                case 78:
                    yyst[yysp] = 78;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 226:
                    yyn = yys78();
                    continue;

                case 79:
                    yyst[yysp] = 79;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 227:
                    yyn = yys79();
                    continue;

                case 80:
                    yyst[yysp] = 80;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 228:
                    yyn = yys80();
                    continue;

                case 81:
                    yyst[yysp] = 81;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 229:
                    yyn = yys81();
                    continue;

                case 82:
                    yyst[yysp] = 82;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 230:
                    switch (yytok) {
                        case '[':
                            yyn = 121;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 83:
                    yyst[yysp] = 83;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 231:
                    yyn = yys83();
                    continue;

                case 84:
                    yyst[yysp] = 84;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 232:
                    yyn = yys84();
                    continue;

                case 85:
                    yyst[yysp] = 85;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 233:
                    yyn = yys85();
                    continue;

                case 86:
                    yyst[yysp] = 86;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 234:
                    yyn = yys86();
                    continue;

                case 87:
                    yyst[yysp] = 87;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 235:
                    yyn = yys87();
                    continue;

                case 88:
                    yyst[yysp] = 88;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 236:
                    yyn = yys88();
                    continue;

                case 89:
                    yyst[yysp] = 89;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 237:
                    yyn = yys89();
                    continue;

                case 90:
                    yyst[yysp] = 90;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 238:
                    yyn = yys90();
                    continue;

                case 91:
                    yyst[yysp] = 91;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 239:
                    yyn = yys91();
                    continue;

                case 92:
                    yyst[yysp] = 92;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 240:
                    yyn = yys92();
                    continue;

                case 93:
                    yyst[yysp] = 93;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 241:
                    yyn = yys93();
                    continue;

                case 94:
                    yyst[yysp] = 94;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 242:
                    yyn = yys94();
                    continue;

                case 95:
                    yyst[yysp] = 95;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 243:
                    yyn = yys95();
                    continue;

                case 96:
                    yyst[yysp] = 96;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 244:
                    yyn = yys96();
                    continue;

                case 97:
                    yyst[yysp] = 97;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 245:
                    yyn = yys97();
                    continue;

                case 98:
                    yyst[yysp] = 98;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 246:
                    yyn = yys98();
                    continue;

                case 99:
                    yyst[yysp] = 99;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 247:
                    yyn = yys99();
                    continue;

                case 100:
                    yyst[yysp] = 100;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 248:
                    yyn = yys100();
                    continue;

                case 101:
                    yyst[yysp] = 101;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 249:
                    yyn = yys101();
                    continue;

                case 102:
                    yyst[yysp] = 102;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 250:
                    yyn = yys102();
                    continue;

                case 103:
                    yyst[yysp] = 103;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 251:
                    yyn = yys103();
                    continue;

                case 104:
                    yyst[yysp] = 104;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 252:
                    yyn = yys104();
                    continue;

                case 105:
                    yyst[yysp] = 105;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 253:
                    yyn = yys105();
                    continue;

                case 106:
                    yyst[yysp] = 106;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 254:
                    yyn = yys106();
                    continue;

                case 107:
                    yyst[yysp] = 107;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 255:
                    yyn = yys107();
                    continue;

                case 108:
                    yyst[yysp] = 108;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 256:
                    yyn = yys108();
                    continue;

                case 109:
                    yyst[yysp] = 109;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 257:
                    yyn = yys109();
                    continue;

                case 110:
                    yyst[yysp] = 110;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 258:
                    switch (yytok) {
                        case ';':
                        case ',':
                            yyn = yyr28();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 111:
                    yyst[yysp] = 111;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 259:
                    switch (yytok) {
                        case '(':
                            yyn = 81;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 112:
                    yyst[yysp] = 112;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 260:
                    yyn = yys112();
                    continue;

                case 113:
                    yyst[yysp] = 113;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 261:
                    switch (yytok) {
                        case ';':
                            yyn = 130;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 114:
                    yyst[yysp] = 114;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 262:
                    switch (yytok) {
                        case ',':
                            yyn = 72;
                            continue;
                        case ';':
                            yyn = yyr69();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 115:
                    yyst[yysp] = 115;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 263:
                    switch (yytok) {
                        case ')':
                            yyn = yyr60();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 116:
                    yyst[yysp] = 116;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 264:
                    yyn = yys116();
                    continue;

                case 117:
                    yyst[yysp] = 117;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 265:
                    switch (yytok) {
                        case ')':
                            yyn = 132;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 118:
                    yyst[yysp] = 118;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 266:
                    yyn = yys118();
                    continue;

                case 119:
                    yyst[yysp] = 119;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 267:
                    yyn = yys119();
                    continue;

                case 120:
                    yyst[yysp] = 120;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 268:
                    yyn = yys120();
                    continue;

                case 121:
                    yyst[yysp] = 121;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 269:
                    yyn = yys121();
                    continue;

                case 122:
                    yyst[yysp] = 122;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 270:
                    yyn = yys122();
                    continue;

                case 123:
                    yyst[yysp] = 123;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 271:
                    yyn = yys123();
                    continue;

                case 124:
                    yyst[yysp] = 124;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 272:
                    yyn = yys124();
                    continue;

                case 125:
                    yyst[yysp] = 125;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 273:
                    yyn = yys125();
                    continue;

                case 126:
                    yyst[yysp] = 126;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 274:
                    yyn = yys126();
                    continue;

                case 127:
                    yyst[yysp] = 127;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 275:
                    yyn = yys127();
                    continue;

                case 128:
                    yyst[yysp] = 128;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 276:
                    yyn = yys128();
                    continue;

                case 129:
                    yyst[yysp] = 129;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 277:
                    switch (yytok) {
                        case ';':
                            yyn = 137;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 130:
                    yyst[yysp] = 130;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 278:
                    yyn = yys130();
                    continue;

                case 131:
                    yyst[yysp] = 131;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 279:
                    yyn = yys131();
                    continue;

                case 132:
                    yyst[yysp] = 132;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 280:
                    yyn = yys132();
                    continue;

                case 133:
                    yyst[yysp] = 133;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 281:
                    yyn = yys133();
                    continue;

                case 134:
                    yyst[yysp] = 134;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 282:
                    yyn = yys134();
                    continue;

                case 135:
                    yyst[yysp] = 135;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 283:
                    yyn = yys135();
                    continue;

                case 136:
                    yyst[yysp] = 136;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 284:
                    yyn = yys136();
                    continue;

                case 137:
                    yyst[yysp] = 137;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 285:
                    yyn = yys137();
                    continue;

                case 138:
                    yyst[yysp] = 138;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 286:
                    yyn = yys138();
                    continue;

                case 139:
                    yyst[yysp] = 139;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 287:
                    switch (yytok) {
                        case ';':
                            yyn = 144;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 140:
                    yyst[yysp] = 140;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 288:
                    switch (yytok) {
                        case ')':
                            yyn = yyr61();
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 141:
                    yyst[yysp] = 141;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 289:
                    yyn = yys141();
                    continue;

                case 142:
                    yyst[yysp] = 142;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 290:
                    yyn = yys142();
                    continue;

                case 143:
                    yyst[yysp] = 143;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 291:
                    yyn = yys143();
                    continue;

                case 144:
                    yyst[yysp] = 144;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 292:
                    yyn = yys144();
                    continue;

                case 145:
                    yyst[yysp] = 145;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 293:
                    switch (yytok) {
                        case ')':
                            yyn = 146;
                            continue;
                    }
                    yyn = 299;
                    continue;

                case 146:
                    yyst[yysp] = 146;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 294:
                    yyn = yys146();
                    continue;

                case 147:
                    yyst[yysp] = 147;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 295:
                    yyn = yys147();
                    continue;

                case 296:
                    return true;
                case 297:
                    yyerror("stack overflow");
                case 298:
                    return false;
                case 299:
                    yyerror("syntax error");
                    return false;
            }
        }
    }

    protected void yyexpand() {
        int[] newyyst = new int[2*yyst.length];
        Object[] newyysv = new Object[2*yyst.length];
        for (int i=0; i<yyst.length; i++) {
            newyyst[i] = yyst[i];
            newyysv[i] = yysv[i];
        }
        yyst = newyyst;
        yysv = newyysv;
    }

    private int yys25() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr16();
        }
        return 299;
    }

    private int yys26() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr16();
        }
        return 299;
    }

    private int yys27() {
        switch (yytok) {
            case BOOLEAN:
                return 5;
            case DOUBLE:
                return 6;
            case INT:
                return 7;
            case BREAK:
                return 33;
            case CONTINUE:
                return 34;
            case DO:
                return 35;
            case FOR:
                return 36;
            case IDENT:
                return 37;
            case IF:
                return 38;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case PRINT:
                return 41;
            case RETURN:
                return 42;
            case WHILE:
                return 43;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case ';':
                return 48;
            case '{':
                return 49;
            case '}':
                return 50;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys28() {
        switch (yytok) {
            case BOOLEAN:
                return 5;
            case DOUBLE:
                return 6;
            case INT:
                return 7;
            case BREAK:
                return 33;
            case CONTINUE:
                return 34;
            case DO:
                return 35;
            case FOR:
                return 36;
            case IDENT:
                return 37;
            case IF:
                return 38;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case PRINT:
                return 41;
            case RETURN:
                return 42;
            case WHILE:
                return 43;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case ';':
                return 48;
            case '{':
                return 49;
            case '~':
                return 51;
            case '}':
                return 52;
        }
        return 299;
    }

    private int yys29() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case ';':
                return 64;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
        }
        return 299;
    }

    private int yys30() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr15();
        }
        return 299;
    }

    private int yys35() {
        switch (yytok) {
            case BOOLEAN:
                return 5;
            case DOUBLE:
                return 6;
            case INT:
                return 7;
            case BREAK:
                return 33;
            case CONTINUE:
                return 34;
            case DO:
                return 35;
            case FOR:
                return 36;
            case IDENT:
                return 37;
            case IF:
                return 38;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case PRINT:
                return 41;
            case RETURN:
                return 42;
            case WHILE:
                return 43;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case ';':
                return 48;
            case '{':
                return 49;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys37() {
        switch (yytok) {
            case '(':
                return 78;
            case '=':
                return 79;
            case '*':
            case LOR:
            case ')':
            case '+':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '[':
            case '>':
            case '<':
            case GTE:
            case ';':
            case '/':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr56();
        }
        return 299;
    }

    private int yys39() {
        switch (yytok) {
            case '*':
            case LOR:
            case ')':
            case '+':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '[':
            case '>':
            case '<':
            case GTE:
            case ';':
            case '/':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr57();
        }
        return 299;
    }

    private int yys41() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys42() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
            case ';':
                return 85;
        }
        return 299;
    }

    private int yys44() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys45() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys46() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys47() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys48() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr17();
        }
        return 299;
    }

    private int yys49() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr16();
        }
        return 299;
    }

    private int yys51() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys53() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys54() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys55() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys56() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys57() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys58() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys59() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys60() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys61() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys62() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys63() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys64() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr19();
        }
        return 299;
    }

    private int yys65() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys66() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys67() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys68() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys69() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys73() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr18();
        }
        return 299;
    }

    private int yys74() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr64();
        }
        return 299;
    }

    private int yys75() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr65();
        }
        return 299;
    }

    private int yys77() {
        switch (yytok) {
            case BOOLEAN:
                return 5;
            case DOUBLE:
                return 6;
            case INT:
                return 7;
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
            case ';':
                return yyr71();
        }
        return 299;
    }

    private int yys78() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
            case ')':
                return yyr59();
        }
        return 299;
    }

    private int yys79() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys80() {
        switch (yytok) {
            case BOOLEAN:
                return 5;
            case DOUBLE:
                return 6;
            case INT:
                return 7;
            case BREAK:
                return 33;
            case CONTINUE:
                return 34;
            case DO:
                return 35;
            case FOR:
                return 36;
            case IDENT:
                return 37;
            case IF:
                return 38;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case PRINT:
                return 41;
            case RETURN:
                return 42;
            case WHILE:
                return 43;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case ';':
                return 48;
            case '{':
                return 49;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys81() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys83() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case ';':
                return 122;
        }
        return 299;
    }

    private int yys84() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case ';':
                return 123;
        }
        return 299;
    }

    private int yys85() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr25();
        }
        return 299;
    }

    private int yys86() {
        switch (yytok) {
            case BOOLEAN:
                return 5;
            case DOUBLE:
                return 6;
            case INT:
                return 7;
            case BREAK:
                return 33;
            case CONTINUE:
                return 34;
            case DO:
                return 35;
            case FOR:
                return 36;
            case IDENT:
                return 37;
            case IF:
                return 38;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case PRINT:
                return 41;
            case RETURN:
                return 42;
            case WHILE:
                return 43;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case ';':
                return 48;
            case '{':
                return 49;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys87() {
        switch (yytok) {
            case '[':
                return 67;
            case '+':
            case '*':
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '>':
            case '<':
            case GTE:
            case ';':
            case '/':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr34();
        }
        return 299;
    }

    private int yys88() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case ')':
                return 125;
        }
        return 299;
    }

    private int yys89() {
        switch (yytok) {
            case '[':
                return 67;
            case '+':
            case '*':
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '>':
            case '<':
            case GTE:
            case ';':
            case '/':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr33();
        }
        return 299;
    }

    private int yys90() {
        switch (yytok) {
            case '[':
                return 67;
            case '+':
            case '*':
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '>':
            case '<':
            case GTE:
            case ';':
            case '/':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr32();
        }
        return 299;
    }

    private int yys91() {
        switch (yytok) {
            case BOOLEAN:
                return 5;
            case DOUBLE:
                return 6;
            case INT:
                return 7;
            case BREAK:
                return 33;
            case CONTINUE:
                return 34;
            case DO:
                return 35;
            case FOR:
                return 36;
            case IDENT:
                return 37;
            case IF:
                return 38;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case PRINT:
                return 41;
            case RETURN:
                return 42;
            case WHILE:
                return 43;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case ';':
                return 48;
            case '{':
                return 49;
            case '~':
                return 51;
            case '}':
                return 126;
        }
        return 299;
    }

    private int yys92() {
        switch (yytok) {
            case '[':
                return 67;
            case '+':
            case '*':
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '>':
            case '<':
            case GTE:
            case ';':
            case '/':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr35();
        }
        return 299;
    }

    private int yys93() {
        switch (yytok) {
            case GTE:
                return 54;
            case LTE:
                return 57;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case ';':
            case NEQ:
            case ',':
                return yyr45();
        }
        return 299;
    }

    private int yys94() {
        switch (yytok) {
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '[':
                return 67;
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '>':
            case '<':
            case GTE:
            case ';':
            case NEQ:
            case ',':
            case LTE:
                return yyr43();
        }
        return 299;
    }

    private int yys95() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case LOR:
            case ')':
            case LAND:
            case ']':
            case ';':
            case ',':
                return yyr49();
        }
        return 299;
    }

    private int yys96() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case LOR:
            case ')':
            case ']':
            case ';':
            case ',':
                return yyr50();
        }
        return 299;
    }

    private int yys97() {
        switch (yytok) {
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '[':
                return 67;
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '>':
            case '<':
            case GTE:
            case ';':
            case NEQ:
            case ',':
            case LTE:
                return yyr42();
        }
        return 299;
    }

    private int yys98() {
        switch (yytok) {
            case GTE:
                return 54;
            case LTE:
                return 57;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case ';':
            case NEQ:
            case ',':
                return yyr44();
        }
        return 299;
    }

    private int yys99() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case LOR:
            case ')':
            case LAND:
            case '&':
            case '|':
            case '^':
            case ']':
            case ';':
            case ',':
                return yyr46();
        }
        return 299;
    }

    private int yys100() {
        switch (yytok) {
            case '[':
                return 67;
            case '+':
            case '*':
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '>':
            case '<':
            case GTE:
            case ';':
            case '/':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr38();
        }
        return 299;
    }

    private int yys101() {
        switch (yytok) {
            case '*':
                return 60;
            case '/':
                return 63;
            case '[':
                return 67;
            case '+':
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '>':
            case '<':
            case GTE:
            case ';':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr36();
        }
        return 299;
    }

    private int yys102() {
        switch (yytok) {
            case '*':
                return 60;
            case '/':
                return 63;
            case '[':
                return 67;
            case '+':
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '>':
            case '<':
            case GTE:
            case ';':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr37();
        }
        return 299;
    }

    private int yys103() {
        switch (yytok) {
            case '[':
                return 67;
            case '+':
            case '*':
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '>':
            case '<':
            case GTE:
            case ';':
            case '/':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr39();
        }
        return 299;
    }

    private int yys104() {
        switch (yytok) {
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '[':
                return 67;
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '>':
            case '<':
            case GTE:
            case ';':
            case NEQ:
            case ',':
            case LTE:
                return yyr40();
        }
        return 299;
    }

    private int yys105() {
        switch (yytok) {
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '[':
                return 67;
            case LOR:
            case ')':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '>':
            case '<':
            case GTE:
            case ';':
            case NEQ:
            case ',':
            case LTE:
                return yyr41();
        }
        return 299;
    }

    private int yys106() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case ']':
                return 127;
        }
        return 299;
    }

    private int yys107() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case LOR:
            case ')':
            case LAND:
            case '|':
            case '^':
            case ']':
            case ';':
            case ',':
                return yyr48();
        }
        return 299;
    }

    private int yys108() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case LOR:
            case ')':
            case LAND:
            case '|':
            case ']':
            case ';':
            case ',':
                return yyr47();
        }
        return 299;
    }

    private int yys109() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys112() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case ';':
                return yyr70();
        }
        return 299;
    }

    private int yys116() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case ',':
                return 131;
            case ')':
                return yyr62();
        }
        return 299;
    }

    private int yys118() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case ')':
            case ']':
            case ';':
            case ',':
                return yyr52();
        }
        return 299;
    }

    private int yys119() {
        switch (yytok) {
            case ELSE:
                return 133;
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr22();
        }
        return 299;
    }

    private int yys120() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case ')':
                return 134;
        }
        return 299;
    }

    private int yys121() {
        switch (yytok) {
            case ']':
                return 13;
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys122() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr23();
        }
        return 299;
    }

    private int yys123() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr26();
        }
        return 299;
    }

    private int yys124() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr20();
        }
        return 299;
    }

    private int yys125() {
        switch (yytok) {
            case '*':
            case LOR:
            case ')':
            case '+':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '[':
            case '>':
            case '<':
            case GTE:
            case ';':
            case '/':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr58();
        }
        return 299;
    }

    private int yys126() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr24();
        }
        return 299;
    }

    private int yys127() {
        switch (yytok) {
            case '=':
                return 136;
            case '*':
            case LOR:
            case ')':
            case '+':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '[':
            case '>':
            case '<':
            case GTE:
            case ';':
            case '/':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr54();
        }
        return 299;
    }

    private int yys128() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case ';':
            case ',':
                return yyr29();
        }
        return 299;
    }

    private int yys130() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
            case ';':
                return yyr68();
        }
        return 299;
    }

    private int yys131() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys132() {
        switch (yytok) {
            case '*':
            case LOR:
            case ')':
            case '+':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '[':
            case '>':
            case '<':
            case GTE:
            case ';':
            case '/':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr51();
        }
        return 299;
    }

    private int yys133() {
        switch (yytok) {
            case BOOLEAN:
                return 5;
            case DOUBLE:
                return 6;
            case INT:
                return 7;
            case BREAK:
                return 33;
            case CONTINUE:
                return 34;
            case DO:
                return 35;
            case FOR:
                return 36;
            case IDENT:
                return 37;
            case IF:
                return 38;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case PRINT:
                return 41;
            case RETURN:
                return 42;
            case WHILE:
                return 43;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case ';':
                return 48;
            case '{':
                return 49;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys134() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case CONTINUE:
            case '~':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case BREAK:
            case BOOLEAN:
                return yyr31();
        }
        return 299;
    }

    private int yys135() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case ']':
                return 142;
        }
        return 299;
    }

    private int yys136() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys137() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr66();
        }
        return 299;
    }

    private int yys138() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case ')':
            case ';':
                return yyr67();
        }
        return 299;
    }

    private int yys141() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr21();
        }
        return 299;
    }

    private int yys142() {
        switch (yytok) {
            case '*':
            case LOR:
            case ')':
            case '+':
            case LAND:
            case EQL:
            case '&':
            case '|':
            case '^':
            case ']':
            case '[':
            case '>':
            case '<':
            case GTE:
            case ';':
            case '/':
            case NEQ:
            case '-':
            case ',':
            case LTE:
                return yyr55();
        }
        return 299;
    }

    private int yys143() {
        switch (yytok) {
            case EQL:
                return 53;
            case GTE:
                return 54;
            case LAND:
                return 55;
            case LOR:
                return 56;
            case LTE:
                return 57;
            case NEQ:
                return 58;
            case '&':
                return 59;
            case '*':
                return 60;
            case '+':
                return 61;
            case '-':
                return 62;
            case '/':
                return 63;
            case '<':
                return 65;
            case '>':
                return 66;
            case '[':
                return 67;
            case '^':
                return 68;
            case '|':
                return 69;
            case ')':
            case ']':
            case ';':
            case ',':
                return yyr53();
        }
        return 299;
    }

    private int yys144() {
        switch (yytok) {
            case IDENT:
                return 37;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case '~':
                return 51;
            case ')':
                return yyr68();
        }
        return 299;
    }

    private int yys146() {
        switch (yytok) {
            case BOOLEAN:
                return 5;
            case DOUBLE:
                return 6;
            case INT:
                return 7;
            case BREAK:
                return 33;
            case CONTINUE:
                return 34;
            case DO:
                return 35;
            case FOR:
                return 36;
            case IDENT:
                return 37;
            case IF:
                return 38;
            case INTLIT:
                return 39;
            case NEW:
                return 40;
            case PRINT:
                return 41;
            case RETURN:
                return 42;
            case WHILE:
                return 43;
            case '!':
                return 44;
            case '(':
                return 45;
            case '+':
                return 46;
            case '-':
                return 47;
            case ';':
                return 48;
            case '{':
                return 49;
            case '~':
                return 51;
        }
        return 299;
    }

    private int yys147() {
        switch (yytok) {
            case '+':
            case '(':
            case '!':
            case INTLIT:
            case WHILE:
            case INT:
            case ELSE:
            case CONTINUE:
            case '}':
            case IF:
            case '{':
            case RETURN:
            case IDENT:
            case DOUBLE:
            case PRINT:
            case NEW:
            case ';':
            case '-':
            case FOR:
            case DO:
            case '~':
            case BREAK:
            case BOOLEAN:
                return yyr63();
        }
        return 299;
    }

    private int yyr1() { // program : fundefs
        { program = ((Fundefs)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return 1;
    }

    private int yyr61() { // args1 : expr ',' args1
        { yyrv = new Args(((Expr)yysv[yysp-3]), ((Args)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypargs1();
    }

    private int yyr62() { // args1 : expr
        { yyrv = new Args(((Expr)yysv[yysp-1]), null); }
        yysv[yysp-=1] = yyrv;
        return yypargs1();
    }

    private int yypargs1() {
        switch (yyst[yysp-1]) {
            case 78: return 115;
            default: return 140;
        }
    }

    private int yyr32() { // expr : '-' expr
        { yyrv = new UMinus(((Expr)yysv[yysp-1])); }
        yysv[yysp-=2] = yyrv;
        return yypexpr();
    }

    private int yyr33() { // expr : '+' expr
        { yyrv = new UPlus(((Expr)yysv[yysp-1])); }
        yysv[yysp-=2] = yyrv;
        return yypexpr();
    }

    private int yyr34() { // expr : '!' expr
        { yyrv = new LNot(((Expr)yysv[yysp-1])); }
        yysv[yysp-=2] = yyrv;
        return yypexpr();
    }

    private int yyr35() { // expr : '~' expr
        { yyrv = new BNot(((Expr)yysv[yysp-1])); }
        yysv[yysp-=2] = yyrv;
        return yypexpr();
    }

    private int yyr36() { // expr : expr '+' expr
        { yyrv = new Add(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr37() { // expr : expr '-' expr
        { yyrv = new Sub(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr38() { // expr : expr '*' expr
        { yyrv = new Mul(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr39() { // expr : expr '/' expr
        { yyrv = new Div(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr40() { // expr : expr '<' expr
        { yyrv = new Lt(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr41() { // expr : expr '>' expr
        { yyrv = new Gt(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr42() { // expr : expr LTE expr
        { yyrv = new Lte(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr43() { // expr : expr GTE expr
        { yyrv = new Gte(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr44() { // expr : expr NEQ expr
        { yyrv = new Neq(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr45() { // expr : expr EQL expr
        { yyrv = new Eql(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr46() { // expr : expr '&' expr
        { yyrv = new BAnd(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr47() { // expr : expr '|' expr
        { yyrv = new BOr(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr48() { // expr : expr '^' expr
        { yyrv = new BXor(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr49() { // expr : expr LAND expr
        { yyrv = new LAnd(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr50() { // expr : expr LOR expr
        { yyrv = new LOr(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr51() { // expr : IDENT '(' args ')'
        { yyrv = new Call(((Id)yysv[yysp-4]), ((Args)yysv[yysp-2])); }
        yysv[yysp-=4] = yyrv;
        return yypexpr();
    }

    private int yyr52() { // expr : IDENT '=' expr
        { yyrv = new Assign(((Id)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yyr53() { // expr : expr '[' expr ']' '=' expr
        { yyrv = new ArrayAssign(((Expr)yysv[yysp-6]), ((Expr)yysv[yysp-4]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=6] = yyrv;
        return yypexpr();
    }

    private int yyr54() { // expr : expr '[' expr ']'
        { yyrv = new ArrayAccess(((Expr)yysv[yysp-4]), ((Expr)yysv[yysp-2])); }
        yysv[yysp-=4] = yyrv;
        return yypexpr();
    }

    private int yyr55() { // expr : NEW type '[' expr ']'
        { yyrv = new NewArray(((Type)yysv[yysp-4]), ((Expr)yysv[yysp-2])); }
        yysv[yysp-=5] = yyrv;
        return yypexpr();
    }

    private int yyr56() { // expr : IDENT
        { yyrv = ((Id)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yypexpr();
    }

    private int yyr57() { // expr : INTLIT
        { yyrv = ((IntLit)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yypexpr();
    }

    private int yyr58() { // expr : '(' expr ')'
        { yyrv = ((Expr)yysv[yysp-2]); }
        yysv[yysp-=3] = yyrv;
        return yypexpr();
    }

    private int yypexpr() {
        switch (yyst[yysp-1]) {
            case 144: return 138;
            case 136: return 143;
            case 131: return 116;
            case 130: return 138;
            case 121: return 135;
            case 109: return 128;
            case 81: return 120;
            case 79: return 118;
            case 78: return 116;
            case 77: return 112;
            case 69: return 108;
            case 68: return 107;
            case 67: return 106;
            case 66: return 105;
            case 65: return 104;
            case 63: return 103;
            case 62: return 102;
            case 61: return 101;
            case 60: return 100;
            case 59: return 99;
            case 58: return 98;
            case 57: return 97;
            case 56: return 96;
            case 55: return 95;
            case 54: return 94;
            case 53: return 93;
            case 51: return 92;
            case 47: return 90;
            case 46: return 89;
            case 45: return 88;
            case 44: return 87;
            case 42: return 84;
            case 41: return 83;
            default: return 29;
        }
    }

    private int yyr10() { // formal : type IDENT
        { yyrv = new Formal(((Type)yysv[yysp-2]), ((Id)yysv[yysp-1]));   }
        yysv[yysp-=2] = yyrv;
        return 15;
    }

    private int yyr6() { // formals : /* empty */
        { yyrv = null; }
        yysv[yysp-=0] = yyrv;
        return yypformals();
    }

    private int yyr7() { // formals : formals1
        { yyrv = ((Formals)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yypformals();
    }

    private int yypformals() {
        switch (yyst[yysp-1]) {
            case 12: return 16;
            default: return 19;
        }
    }

    private int yyr8() { // formals1 : formal ',' formals1
        { yyrv = new Formals(((Formal)yysv[yysp-3]), ((Formals)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypformals1();
    }

    private int yyr9() { // formals1 : formal
        { yyrv = new Formals(((Formal)yysv[yysp-1]), null); }
        yysv[yysp-=1] = yyrv;
        return yypformals1();
    }

    private int yypformals1() {
        switch (yyst[yysp-1]) {
            case 20: return 24;
            default: return 17;
        }
    }

    private int yyr4() { // fundef : type IDENT '(' formals ')' '{' stmts '}'
        { yyrv = new Fundef(((Type)yysv[yysp-8]), ((Id)yysv[yysp-7]), ((Formals)yysv[yysp-5]), ((Stmts)yysv[yysp-2])); }
        yysv[yysp-=8] = yyrv;
        return 3;
    }

    private int yyr5() { // fundef : VOID IDENT '(' formals ')' '{' stmts '}'
        { yyrv = new Fundef(null, ((Id)yysv[yysp-7]), ((Formals)yysv[yysp-5]), ((Stmts)yysv[yysp-2])); }
        yysv[yysp-=8] = yyrv;
        return 3;
    }

    private int yyr2() { // fundefs : fundefs fundef
        { yyrv = new Fundefs(((Fundef)yysv[yysp-1]), ((Fundefs)yysv[yysp-2])); }
        yysv[yysp-=2] = yyrv;
        return 2;
    }

    private int yyr3() { // fundefs : /* empty */
        { yyrv = null; }
        yysv[yysp-=0] = yyrv;
        return 2;
    }

    private int yyr69() { // init : varDecl
        { yyrv = new VarDeclInit(((VarDecl)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        return 113;
    }

    private int yyr70() { // init : expr
        { yyrv = new ExprInit(((Expr)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        return 113;
    }

    private int yyr71() { // init : /* empty */
        { yyrv = new NoInit(); }
        yysv[yysp-=0] = yyrv;
        return 113;
    }

    private int yyr67() { // optExpr : expr
        { yyrv = ((Expr)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yypoptExpr();
    }

    private int yyr68() { // optExpr : /* empty */
        { yyrv = null; }
        yysv[yysp-=0] = yyrv;
        return yypoptExpr();
    }

    private int yypoptExpr() {
        switch (yyst[yysp-1]) {
            case 130: return 139;
            default: return 145;
        }
    }

    private int yyr59() { // args : /* empty */
        { yyrv = null; }
        yysv[yysp-=0] = yyrv;
        return 117;
    }

    private int yyr60() { // args : args1
        { yyrv = ((Args)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return 117;
    }

    private int yyr17() { // stmt : ';'
        { yyrv = new Empty(); }
        yysv[yysp-=1] = yyrv;
        return yypstmt();
    }

    private int yyr18() { // stmt : varDecl ';'
        { yyrv = ((VarDecl)yysv[yysp-2]); }
        yysv[yysp-=2] = yyrv;
        return yypstmt();
    }

    private int yyr19() { // stmt : expr ';'
        { yyrv = new ExprStmt(((Expr)yysv[yysp-2])); }
        yysv[yysp-=2] = yyrv;
        return yypstmt();
    }

    private int yyr20() { // stmt : WHILE test stmt
        { yyrv = new While(((Expr)yysv[yysp-2]), ((Stmt)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypstmt();
    }

    private int yyr21() { // stmt : IF test stmt ELSE stmt
        { yyrv = new If(((Expr)yysv[yysp-4]), ((Stmt)yysv[yysp-3]), ((Stmt)yysv[yysp-1])); }
        yysv[yysp-=5] = yyrv;
        return yypstmt();
    }

    private int yyr22() { // stmt : IF test stmt
        { yyrv = new If(((Expr)yysv[yysp-2]), ((Stmt)yysv[yysp-1]), new Empty()); }
        yysv[yysp-=3] = yyrv;
        return yypstmt();
    }

    private int yyr23() { // stmt : PRINT expr ';'
        { yyrv = new Print(((Expr)yysv[yysp-2])); }
        yysv[yysp-=3] = yyrv;
        return yypstmt();
    }

    private int yyr24() { // stmt : '{' stmts '}'
        { yyrv = new Block(((Stmts)yysv[yysp-2])); }
        yysv[yysp-=3] = yyrv;
        return yypstmt();
    }

    private int yyr25() { // stmt : RETURN ';'
        { yyrv = new Return(); }
        yysv[yysp-=2] = yyrv;
        return yypstmt();
    }

    private int yyr26() { // stmt : RETURN expr ';'
        { yyrv = new Return(((Expr)yysv[yysp-2])); }
        yysv[yysp-=3] = yyrv;
        return yypstmt();
    }

    private int yyr63() { // stmt : FOR '(' init ';' optExpr ';' optExpr ')' stmt
        { yyrv = new For(((ForInit)yysv[yysp-7]), ((Expr)yysv[yysp-5]), ((Expr)yysv[yysp-3]), ((Stmt)yysv[yysp-1])); }
        yysv[yysp-=9] = yyrv;
        return yypstmt();
    }

    private int yyr64() { // stmt : BREAK ';'
        { yyrv = new Break(); }
        yysv[yysp-=2] = yyrv;
        return yypstmt();
    }

    private int yyr65() { // stmt : CONTINUE ';'
        { yyrv = new Continue(); }
        yysv[yysp-=2] = yyrv;
        return yypstmt();
    }

    private int yyr66() { // stmt : DO stmt WHILE test ';'
        { yyrv = new DoWhile(((Stmt)yysv[yysp-4]), ((Expr)yysv[yysp-2])); }
        yysv[yysp-=5] = yyrv;
        return yypstmt();
    }

    private int yypstmt() {
        switch (yyst[yysp-1]) {
            case 146: return 147;
            case 133: return 141;
            case 86: return 124;
            case 80: return 119;
            case 35: return 76;
            default: return 30;
        }
    }

    private int yyr15() { // stmts : stmts stmt
        { yyrv = ((Stmts)yysv[yysp-2]).addStmt(((Stmt)yysv[yysp-1])); }
        yysv[yysp-=2] = yyrv;
        return yypstmts();
    }

    private int yyr16() { // stmts : /* empty */
        { yyrv = new Stmts(); }
        yysv[yysp-=0] = yyrv;
        return yypstmts();
    }

    private int yypstmts() {
        switch (yyst[yysp-1]) {
            case 26: return 28;
            case 25: return 27;
            default: return 91;
        }
    }

    private int yyr31() { // test : '(' expr ')'
        { yyrv = ((Expr)yysv[yysp-2]); }
        yysv[yysp-=3] = yyrv;
        switch (yyst[yysp-1]) {
            case 43: return 86;
            case 38: return 80;
            default: return 129;
        }
    }

    private int yyr11() { // type : INT
        { yyrv = new IntType(); }
        yysv[yysp-=1] = yyrv;
        return yyptype();
    }

    private int yyr12() { // type : BOOLEAN
        { yyrv = new BooleanType(); }
        yysv[yysp-=1] = yyrv;
        return yyptype();
    }

    private int yyr13() { // type : DOUBLE
        { yyrv = new DoubleType(); }
        yysv[yysp-=1] = yyrv;
        return yyptype();
    }

    private int yyr14() { // type : type '[' ']'
        { yyrv = new ArrayType(((Type)yysv[yysp-3])); }
        yysv[yysp-=3] = yyrv;
        return yyptype();
    }

    private int yyptype() {
        switch (yyst[yysp-1]) {
            case 40: return 82;
            case 20: return 18;
            case 14: return 18;
            case 12: return 18;
            case 2: return 4;
            default: return 31;
        }
    }

    private int yyr27() { // varDecl : type varIntro
        { yyrv = new VarDecl(((Type)yysv[yysp-2]), ((VarIntro)yysv[yysp-1])); }
        yysv[yysp-=2] = yyrv;
        return yypvarDecl();
    }

    private int yyr28() { // varDecl : varDecl ',' varIntro
        { yyrv = ((VarDecl)yysv[yysp-3]).addIntro(((VarIntro)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypvarDecl();
    }

    private int yypvarDecl() {
        switch (yyst[yysp-1]) {
            case 77: return 114;
            default: return 32;
        }
    }

    private int yyr29() { // varIntro : IDENT '=' expr
        { yyrv = new InitVarIntro(((Id)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypvarIntro();
    }

    private int yyr30() { // varIntro : IDENT
        { yyrv = new VarIntro(((Id)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        return yypvarIntro();
    }

    private int yypvarIntro() {
        switch (yyst[yysp-1]) {
            case 31: return 70;
            default: return 110;
        }
    }

    protected String[] yyerrmsgs = {
    };


    private MiniLexer lexer;
    private Fundefs   program;

    public MiniParser(Handler handler, MiniLexer lexer) {
      super(handler);
      this.lexer = lexer;
      lexer.nextToken();
      parse();
    }

    public Fundefs getProgram() {
      return program;
    }

    private void yyerror(String msg) {
      report(new Failure(msg));
    }


}
