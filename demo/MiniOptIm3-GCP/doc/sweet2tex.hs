-------------------------------------------------------------------------------

import System(getArgs)
import Char(isSpace)

data Line = Begin | End | Blank | Text String

classify                   :: String -> Line
classify ('/':'*':'@':_)    = Begin
classify ('@':'*':'/':_)    = End
classify s  | all isSpace s = Blank
            | otherwise     = Text s

main  = do args <- getArgs
           if null args then usage else mapM_ totex args

usage = putStrLn "usage: sweet2tex file ..."

totex file = do let name = stripExt file
                s <- readFile file
                writeFile (name ++ ".tex")
                 $ unlines
                  $ state0
                   $ map classify
                    $ lines s

stripExt name
 = if null tser then name else reverse (tail tser)
   where (xiffus, tser) = span ('.'/=) (reverse name)

-- State machine for translating lists of lines: ------------------------------
-- initial state:
state0 []            = []
state0 (Begin:ls)    = state1 ls
state0 (Blank:ls)    = "" : state0 ls
state0 (End:ls)      = state0 ls  -- shouldn't occur
state0 (Text s : ls) = s : state0 ls

-- processing comment text:
state1 [] = []
state1 (Begin:ls)    = state1 ls
state1 (End:ls)      = state2 ls
state1 (Blank:ls)    = "" : state1 ls
state1 (Text s : ls) = tr0 s : state1 ls

-- looking for program text after an End:
state2 []            = []
state2 (Begin:ls)    = state1 ls
state2 (End:ls)      = state2 ls
state2 (Blank:ls)    = state2 ls
state2 ls            = beginVerbatim : state3 ls

-- processing program text:
state3 []               = [endVerbatim]
state3 (Begin:ls)       = endVerbatim : state1 ls
state3 (End:ls)         = state3 ls
state3 (Blank:Begin:ls) = endVerbatim : state1 ls
state3 (Blank:ls)       = "" : state3 ls
state3 (Text ('/':'/':'!':_) : ls)
                        = state3 ls
state3 (Text s : ls)    = indent s : state3 ls

beginVerbatim = "{\\codesize%\n\\begin{verbatim}"
endVerbatim   = "\\end{verbatim}\n}"
indent s      = "  " ++ s

-- State machine for translating individual lines: ----------------------------

-- outside '@'
tr0 ""           = ""
tr0 ('@':'@':cs) = '@' : tr0 cs
tr0 ('@':cs)     = beginVerb ++ tr1 cs
tr0 (c:cs)       = c : tr0 cs

-- inside '@'
tr1 ""           = endVerb
tr1 ('@':'@':cs) = '@' : tr1 cs
tr1 ('@':cs)     = endVerb ++ tr0 cs
tr1 (c:cs)       = c : tr1 cs

beginVerb = "\\verb!"
endVerb   = "!"

-------------------------------------------------------------------------------
