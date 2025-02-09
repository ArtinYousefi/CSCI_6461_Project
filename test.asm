LOC 10           ; Start at memory location 10
Data 5          ; Store 5 at location 10
Data 8          ; Store 8 at location 11
Data 0          ; Placeholder for computation result
LDX 1,11        ; Load index register X1 with value at location 11 (which is 8)
LDR 2,0,10      ; Load R2 with contents of memory location 10 (which is 5)
LDR 3,1,10      ; Load R3 with contents of memory location (10 + X1)
ADD 2,3,0       ; Add R2 and R3, store in R2
STR 2,0,12      ; Store result at memory location 12
HLT             ; Halt execution
