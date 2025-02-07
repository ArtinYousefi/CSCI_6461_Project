LOC 6           ; Begin at memory location 6
Data 10         ; Store 10 at location 6
Data 3          ; Store 3 at location 7
Data End        ; Store address of "End" at location 8
Data 0
Data 12
Data 9
Data 18
Data 12
LDX 2,7        ; Load index register X2 with value at location 7 (which is 3)
LDR 3,0,10     ; Load R3 with contents of memory location 10
LDR 2,2,10     ; Load R2 with contents of memory location (10 + X2)
LDR 1,2,10,1   ; Load R1 with contents of memory location (EA computed with indirect addressing)
LDA 0,0,0      ; Load register R0 with address 0
LDX 1,8        ; Load X1 with contents of location 8 (which is the address of "End")
JZ 0,1,0       ; Jump to "End" if R0 == 0
LOC 1024
End: HLT       ; Stop execution
