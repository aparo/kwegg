#!/bin/sh
#author: Parag Arora (parag.arora@gmail.com)
#use this script while deploying news reader
stty -echo 
read -p "Enter mysql root password: " password; echo 
stty echo
echo "Creating user kwegg"
mysql -uroot -p$password -e"create user 'kwegg'@'localhost' identified by 'Kwegg123'"
echo "Granting all on *.* to kwegg"
mysql -uroot -p$password -e"grant all on *.* to 'kwegg'@'localhost'"