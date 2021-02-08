ipconfig /all > 1.txt 
findstr "[1-9].[1-9].[1-9].[1-9]" 1.txt >2.txt
findstr "IPv4" 2.txt > README.md
del 1.txt
del 2.txt


git add --all
git commit -m README.md
git push
