# Patchwork
La méthode main est dans le fichier "SimpleGameController.java"
**1 Choix du mode**  
Au début du jeu vous devez entre la version console avec uniquement des affichages dans la console et la version avec une interface graphique.

Ensuite vous devez choisir un mode vous avez le choix entre le mode simplifé où tous les patch sont des carrés 2 par 2 et où il n'y a pas de carré 1 par 1 et de bonus pour les carrés 7 par 7.

Si vous choisissez la version normale vous devrez choisir de jouer avec les pièces du jeu original ou d'importer un fichier personalisé contenant les pièces sous ce format:


Le mode normal comprend tous les éléments officiel du jeu, on peut tourner les pièces avant de les poser et on a les 4 vraies pièces du jeu.

**2 Time Board**  
Le timeBoard représente le temps et est également le plateau sur lequel les joueur se déplace au cours du jeu.

Dans la version console:
	<br>-Le '1' indique la position du joueur 1 et le '2' indique la position du joueur 2.
	<br>-Les 'B' indique la position des boutons et les 'S' indique la position des carrés.
Dans la version avec interface graphique:
	<br>-Le carré bleu indique le joueur 1 et le carré jaune indique le joueur 2.

Le timeBoard est de hauteur 6 et de largeur 9, il se parcours de gauche à droite et de haut en bas, la première case de la première ligne est la première case du timeBoard et la première case de la deuxième ligne est la dixième case du tableau qui comporte 54 cases.

**3 QuiltBoard**  
Le quiltBoard est de taille 9 * 9 et ses lignes et colonnes sont numérotés de 0 à 9.
En dessous du quiltBoard le nombre total de boutons de ses patchs est affiché dans la version console.

**4 Les patchs**  
Les patchs ont un nombre de boutons, un prix, et un temps qui correspond au nombre de case qu'on doit parcourir après l'achat d'un patch.
Pour acheter un patch il faut avoir assez de boutons, après l'achat on vous le demmande de choisir une position correspondant à l'angle en haut de la forme du patch après la rotation les autres cases seront positionnées en dessous ou à gauche de cette position.
On vous demmande également une rotation, dans l'interface graphique il suffit de cliquer sur la rotation voulue, voici les commandes pour choisir cette rotation dans la console:  
	-0 pour ne pas tourner  
	-1 pour tourner à droite  
	-2 pour retourner le patch  
	-3 pour tourner à gauche  

**5 Déroulement d'un tour**  
Au début du tour<br>
Dans la version console:
	<br>-Le timeBoard ainsi que les infos des joueurs et leurs quiltBoard s'affiche dans la console.
	<br>-Pendant un tour vous pouvez décider d'acheter un patch en tapant '1','2','3' en fonction du patch qui vous intéresse ou vous pouvez taper n'importe quel autre nombre pour aller dépasser l'autre joueur.
Dans la version avec interface graphique;
	<br>-Le timeBoard, le quiltboard et la liste des patchs s'affiche.
	<br>-Vous pouvez décider d'acheter un patch en cliquant dessus ou de passer votre tour et dépasser l'autre joueur en cliquant sur "skip your turn".

**6 Les déplacements**  
Quand vous vous déplacer pour rattraper l'autre joueur vous gagnez un bouton par case parcouru alors que quand vous vous déplacez après l'achat d'un patch vous ne gagnez pas de boutons en fonction du nombre de cases parcourues.
Quand vous passez sur un boutons vous gagnez le nombre de boutons présent sur votre quiltBoard.
Quand vous passez sur un carré, le carré s'enlève du terrain et vous pouvez le placer sur votre quiltBoard

**7 Bonus du carré 7 par 7**  
Quand vous réussissez à assembler un carré de 7 par 7 sur votre quiltBoard vous gagnez un bonus qui vous accorde 7 points de plus sur votre score en fin de partie que votre adversaire ne pourra plus obtenir après vous.

**8 Fin de partie**  
Le jeu s'arrête quand les 2 joueurs ont atteint la cinquante quatrième case du timeBoard. Quand le premier joueur est arrivé le deuxième joueur continue de jouer jusqu'à le rattraper.

**9 Le score**
<br>À la fin du jeu on procède au décompte du score.
Votre score final est la somme du nombre de boutons que vous possédez et du bonus du carré 7 par 7 si vous l'avez obtenu moins 2 fois le nombre de trous sur votre quiltBoard.
Après le décompte du score le gagnant de la partie s'affiche dans la console.