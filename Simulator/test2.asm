        LOC 100       ; Start program at memory location 100

READ:   LDX 1, 200   ; Load memory location 200 (start of paragraph)
        IN R0, 2     ; Read first sentence from file into R0
        STR R0, 1, 0 ; Store at memory[200]
        IN R0, 2     ; Read second sentence
        STR R0, 1, 1
        IN R0, 2
        STR R0, 1, 2
        IN R0, 2
        STR R0, 1, 3
        IN R0, 2
        STR R0, 1, 4
        IN R0, 2
        STR R0, 1, 5

PRINT:  LDX 1, 200   ; Load base address of paragraph
        LDR R0, 1, 0 ; Load first sentence
        OUT R0, 1    ; Print first sentence
        LDR R0, 1, 1
        OUT R0, 1
        LDR R0, 1, 2
        OUT R0, 1
        LDR R0, 1, 3
        OUT R0, 1
        LDR R0, 1, 4
        OUT R0, 1
        LDR R0, 1, 5
        OUT R0, 1

SEARCH: IN R0, 0     ; Read word from user
        LDX 2, 200   ; Load memory location of paragraph
        LDR R1, 2, 0 ; Load first word
        TRR R1, R0   ; Compare with input
        JZ R1, 0, 150 ; If match, jump to print match

        LDR R1, 2, 1
        TRR R1, R0
        JZ R1, 0, 151

        LDR R1, 2, 2
        TRR R1, R0
        JZ R1, 0, 152

        LDR R1, 2, 3
        TRR R1, R0
        JZ R1, 0, 153

        LDR R1, 2, 4
        TRR R1, R0
        JZ R1, 0, 154

        LDR R1, 2, 5
        TRR R1, R0
        JZ R1, 0, 155

        JMA 200       ; If no match found, jump to end

150:    LDA R0, 0, 1  ; Load sentence number
        OUT R0, 1     ; Print sentence number
        JMA 200
151:    LDA R0, 0, 2
        OUT R0, 1
        JMA 200
152:    LDA R0, 0, 3
        OUT R0, 1
        JMA 200
153:    LDA R0, 0, 4
        OUT R0, 1
        JMA 200
154:    LDA R0, 0, 5
        OUT R0, 1
        JMA 200
155:    LDA R0, 0, 6
        OUT R0, 1

200:    HLT           ; Halt program
