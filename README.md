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

'M' -> Un monstre, a l'avenir chaque monstre aura un caractère different.


Ce qui est fait :  
* Génération du monde et des maps en fonction d'une Seed (pour une même Seed, on obtiendra toujours la même map). Il existe toujours un chemin entre l'entrée du map et sa sortie
* Combat au tour par tour avec 2 types d'attaques différentes et 1 seul type de monstre.
* Gestion de l'inventaire
* Gestion de l'experience  
* Le(s) monstre(s) sont capables de se déplacé jusqu'au joueurs

  
Prévu :  
* Des biomes différents sur les maps ( les maps ont certes des biomes differents, mais pas assez homogènes)  
* Des monstres diffèrents en fonction des biomes ( Cerf qui fuit le joueur dans les forêt, Poisson qui attaque le joueurs dans les lacs, des Boss)  
* Courbes d'experience ( actuellement 100exp = 1 level, qu'importe le level du joueurs)
* Des loot sur les monstres, ainsi que d'autres moyens d'obtenir des items ( marchand / trésor )  
* Une fin ( actuellement il n'y a pas de fin, une fois arrivé sur la dernière map il ne se passe rien, l'objectif est que la dernière map fasse affronter un Boss, qui, si battu regènere un monde avec des monstres plus haut niveau)
* Des interactions avec les diffèrentes cases, les cases ',' peuvent déclenché un combat contre des petits monstres ( comme pokemon), une competence pour se déplacé sur des Lacs, une competence pour couper un arbre.  
  
    
Problèmes :  
* Le spawn du monstre n'est pas encore géré, il apparait au hasard sur la map, parfois entre quatres cases le bloquant ( toutefois, nous n'avons pas encore créé la classe déterminant où apparaissent les monstres, ce n'est pas un problème, dans le sens où il s'agit surtout d'une fonctionnalité pas encore implémenté, mais les monstres sont present sur la map à titre de test)
* Les biomes généré actuellement ne sont pas satisfaisant, trop aléatoires, pas assez homogènes  
* Les couloirs apparaissent parfois avec vue sur le vides, le joueur ne peux certes pas s'y déplacé, mais la vue sur le vide n'est pas vraiment prévue. ( arrive a chaque fois que le couloirs doit faire un zigzag)

    



