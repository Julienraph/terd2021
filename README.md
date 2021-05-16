# terd

## Comment lancer ce super Rogue Like
Pour lancer le projet, exécutez: `java -jar TERD-<version>.jar`


@ -> Votre personnage
 
'.' -> une case de base

',' -> une case d'herbe, vous pouvez vous déplacez dessus. A l'avenir cette case devrai déclenché des combats comme un jeu Pokemon

'L' -> une case d'eau, vous ne pouvez PAS vous déplacez dessus. A l'avenir une compétence permettra de se déplacez dessus.

'T' -> une case d'arbre, vous ne pouvez PAS vous déplacez dessus. A l'avenir une compétence permettra de couper les arbres.

'X' -> une case de montagne, vous ne pouvez PAS vous déplacez dessus.

'#' -> un mur, vous n'êtes pas un fantome, dommage, vous ne pouvez PAS vous déplacez dessus.

' ' -> du vide, ce monde est vraiment étrange ...   vous ne pouvez PAS vous déplacez dessus.

'M' -> Un monstre OrcWarrior qui se déplace aléatoirement. Récompense avec de l'XP.

'C' -> Un Cerf qui se déplace aléatoirement quand le joueur est hors de la salle (sur un pont) sinon essaye de fuir. Récompense avec de la Viande et de l'XP.

'B' -> Sanglier qui se déplace aléatoirement. Récompense avec de la Viande et de l'XP.

'P' -> Consommable que le joueur peut ramasser en marchant dessus

'O' -> Credit que le joueur peut ramasser en marchant dessus

'D' -> Porte qui tp le joueur vers le niveau suivant, ou qui termine la partie si c'est le niveau final.


Ce qui est fait :  
* Génération procédural du niveau en fonction de la Seed avec un chemin de map d'une longueur variable (pour une même Seed, on obtiendra toujours le même niveau).
* Génération des maps avec des sorties et une hauteur/largeur variables ainsi que son remplissage en fonction de la Seed (pour une même Seed, on obtiendra toujours la même map). Il existe toujours un chemin entre l'entrée d'une map et sa sortie.
* Déplacement des Props dans le niveau
* Affichage de la carte du niveau
* Combat au tour par tour avec 2 types d'attaques différentes et 1 seul type de monstre.
* Implémentation des Items des Props (Consommable, Armes, Compétences)
* Implémentation de l'Inventaire et sa gestion qui permet de s'équiper ou d'améliorer une Arme/Compétence ou de se régénérer des PV.
* Gestion de l'experience : Le joueur peut level-up et gagne des crédits.
* Implémentation de(s) monstre(s), qui se déplace aléatoirement sauf le Cerf qui (tente) de fuir.
* Des objets au sol que le joueur peut ramasser (Consommable ou Crédit)
* Un Menu Principal qui permet de reprendre la partie, de relancer la partie, d'obtenir le nom de la seed, de donner une Seed, d'en créer une nouvelle ou de quitter le jeu
* Une porte qui permet de passer d'un niveau à l'autre et une fin
  
Prévu :  
* L'IA de déplacement des monstres.
* L'IA de combat des monstres.
* Des biomes différents sur les maps ( les maps ont certes des biomes differents, mais pas assez homogènes)  
* Des monstres diffèrents en fonction des biomes ( Cerf qui fuit le joueur dans les forêt, Poisson qui attaque le joueurs dans les lacs, des Boss)  
* Courbes d'experience ( actuellement 100exp = 1 level, qu'importe le level du joueurs)
* Un marchands à qui on peut acheter ou vendre
* Des interactions avec les diffèrentes cases, les cases ',' peuvent déclenché un combat contre des petits monstres ( comme pokemon), une competence pour se déplacé sur des Lacs, une competence pour couper un arbre.  
  
    
Problèmes :  
* Les biomes généré actuellement ne sont pas satisfaisant, trop aléatoires, pas assez homogènes  
* Les couloirs apparaissent parfois avec vue sur le vides, le joueur ne peux certes pas s'y déplacé, mais la vue sur le vide n'est pas vraiment prévue. ( arrive a chaque fois que le couloirs doit faire un zigzag)
* Si le joueur donne une Seed, le jeu ne vérifie pas si la Seed est correct. Il vérifie seulement si elle est d'assez grande taille (supérieur à 500 caractères)

    



