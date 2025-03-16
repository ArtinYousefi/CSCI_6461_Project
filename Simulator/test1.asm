        LOC 10      ; Start program at memory location 10

START:  IN R0, 0   ; Read first number from console into R0
        STR R0, 0, 20   ; Store at memory[20]
        IN R1, 0   ; Read second number
        STR R1, 0, 21
        IN R2, 0   ; Read third number
        STR R2, 0, 22
        IN R3, 0   ; Read fourth number
        STR R3, 0, 23
        IN R0, 0
        STR R0, 0, 24
        IN R1, 0
        STR R1, 0, 25
        IN R2, 0
        STR R2, 0, 26
        IN R3, 0
        STR R3, 0, 27
        IN R0, 0
        STR R0, 0, 28
        IN R1, 0
        STR R1, 0, 29
        IN R2, 0
        STR R2, 0, 30
        IN R3, 0
        STR R3, 0, 31
        IN R0, 0
        STR R0, 0, 32
        IN R1, 0
        STR R1, 0, 33
        IN R2, 0
        STR R2, 0, 34
        IN R3, 0
        STR R3, 0, 35
        IN R0, 0
        STR R0, 0, 36
        IN R1, 0
        STR R1, 0, 37
        IN R2, 0
        STR R2, 0, 38
        IN R3, 0
        STR R3, 0, 39

        IN R0, 0   ; Read target number into R0

        LDR R1, 0, 20   ; Load first stored number into R1
        SMR R1, 0, 40   ; R1 = R1 - target number (find difference)
        STR R1, 0, 50   ; Store difference

        LDR R2, 0, 21   ; Load second stored number
        SMR R2, 0, 40   ; Find difference
        STR R2, 0, 51

        ; Compare differences to find the smallest difference
        LDR R3, 0, 50
        JGE R3, 0, 51
        LDA R0, 0, 20   ; If true, closest number is in 20
        JMA 100         ; Jump to print section
        LDA R0, 0, 21   ; Else, closest is in 21

100:    OUT R0, 1       ; Print closest number
        HLT             ; Halt program

