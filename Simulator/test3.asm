        LOC     100           ; Program starts at address 100


READ:   LDX     1, 200        ; X1 = base address to store paragraph in memory[200]

        IN      R0, 2         ; Sentence 1 from file (Card Reader)
        STR     R0, 1, 0

        IN      R0, 2         ; Sentence 2
        STR     R0, 1, 1

        IN      R0, 2
        STR     R0, 1, 2

        IN      R0, 2
        STR     R0, 1, 3

        IN      R0, 2
        STR     R0, 1, 4

        IN      R0, 2
        STR     R0, 1, 5


PRINT:  LDX     1, 200        ; Reset X1 to base of paragraph

        LDR     R0, 1, 0
        OUT     R0, 1

        LDR     R0, 1, 1
        OUT     R0, 1

        LDR     R0, 1, 2
        OUT     R0, 1

        LDR     R0, 1, 3
        OUT     R0, 1

        LDR     R0, 1, 4
        OUT     R0, 1

        LDR     R0, 1, 5
        OUT     R0, 1


SEARCH: IN      R0, 0         ; User enters word to search
        STR     R0, 0, 250    ; Save word at memory[250]

        LDX     2, 200        ; X2 = pointer to paragraph
        LDR     R1, 2, 0
        TRR     R1, R0
        JZ      R1, 0, MATCH0

        LDR     R1, 2, 1
        TRR     R1, R0
        JZ      R1, 0, MATCH1

        LDR     R1, 2, 2
        TRR     R1, R0
        JZ      R1, 0, MATCH2

        LDR     R1, 2, 3
        TRR     R1, R0
        JZ      R1, 0, MATCH3

        LDR     R1, 2, 4
        TRR     R1, R0
        JZ      R1, 0, MATCH4

        LDR     R1, 2, 5
        TRR     R1, R0
        JZ      R1, 0, MATCH5

        JMA     END           ; If not found, halt


        LOC     150

MATCH0: LDA     R2, 0, 1      ; Sentence number = 1
        OUT     R0, 1         ; Print matching word
        OUT     R2, 1         ; Print sentence number
        JMA     END

MATCH1: LDA     R2, 0, 2
        OUT     R0, 1
        OUT     R2, 1
        JMA     END

MATCH2: LDA     R2, 0, 3
        OUT     R0, 1
        OUT     R2, 1
        JMA     END

MATCH3: LDA     R2, 0, 4
        OUT     R0, 1
        OUT     R2, 1
        JMA     END

MATCH4: LDA     R2, 0, 5
        OUT     R0, 1
        OUT     R2, 1
        JMA     END

MATCH5: LDA     R2, 0, 6
        OUT     R0, 1
        OUT     R2, 1
        JMA     END


END:    HLT


        LOC     200           ; Memory to hold 6 sentences
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0

        LOC     250           ; Word to search (from user)
        Data    0
