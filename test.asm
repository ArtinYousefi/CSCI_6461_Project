LOC 4           ; Begin at memory location 4
Data 22         ; Store 10 at location 5
Data 3          ; Store 3 at location 6
Data 73         ; Store 12 at location 7
Data 9         ; Store 12 at location 8
Data End        ; Store address of "End" at location 9
Data 9
Data 0
LDX 1,6        ; Load index register X1 with value at location 6 (which is 3)
LDR 2,0,7     ; Load R2 with contents of memory location 7
LDR 3,2,8     ; Load R3 with contents of memory location 8
LDR 1,3,8     ; Load R3 with contents of memory location 8
LDX 3,5        ; Load X3 with contents of location 5 
LDR 1,2,7,1   ; Load R1 with contents of memory location (EA computed with indirect addressing)
LDA 0,0,0      ; Load register R0 with address 0
LDX 2,9        ; Load X2 with contents of location 9 (which is the address of "End")
JZ 0,1,0       ; Jump to "End" if R0 == 0
LOC 1024
End: HLT       ; Stop execution
