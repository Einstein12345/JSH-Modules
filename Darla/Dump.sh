#! /bin/sh
hexdump -e '16/1 "%02x, " "\n"' Darla.class > Darla.bin;
echo "Dumped Darla.class";
