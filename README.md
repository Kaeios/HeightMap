L’objectif de ce projet est de réaliser un logiciel qui permet de calculer un plus court chemin entre deux  points  sur  une  carte  qui  représente  des  hauteurs.  Pour  cela,  le  logiciel  doit  permettre  les fonctionnalités suivantes :  

- Créer une carte 
- Charger une carte 
- Editer une carte 
- Sauvegarder une carte 
- Supprimer une carte 
- Calculer un chemin optimal entre deux points 
- Afficher le chemin optimal sur la carte 
- Effectuer toutes les actions depuis des fenêtres 

Pour trouver le plus court chemin entre les deux points, il nous est imposé d’utiliser un algorithme qui fait usage de backtrack. Nous implémenterons cependant également un autre algorithme bien plus efficace  pour  cette  tache  afin  de  pouvoir  obtenir  un  affichage  rapide  et  des  fonctionnalités supplémentaires. 

2. Analyse<a name="_page2_x68.00_y378.92"></a> du projet 

<a name="_page2_x68.00_y425.92"></a>1.  Analyse du problème principal 

La recherche d’un plus court chemin est la partie qui pose le plus de problème pour la réalisation de ce logiciel. Pour simplifier cette rechercher, on peut modéliser les déplacements possibles par un **graphe orienté** avec des arêtes pondérées. Pour construire ce graphe, on procède comme suit :  

1. Créer un sommet pour chaque case de la carte 
1. Relier chaque sommet aux sommets des cases adjacentes par des arêtes orientées 
1. Définir le poids de chaque arrête comme la différence entre la destination et l’origine 
1. Supprimer toutes les arêtes dont le poids est strictement supérieur à 3 en valeur absolue 
1. On change le poids de toutes les arrêtes avec un poids négatif à 0 

Ainsi, on obtient un graphe qui représente tous les déplacements possibles ainsi que leurs coûts. Voici le graphe que l’on obtiendrait pour la carte suivante :  

Carte :   ![](Aspose.Words.00b245a1-3ec5-421a-a72f-ea78ee41fb1a.004.png)

2  4  8  4  

*Figure 1 - Modélisation de la carte en graphe* 

En appliquant l’algorithme de backtrack sur ce graphe plutôt que sur un tableau, il n’est pas nécessaire de définir une condition pour déterminer si une case peut être atteinte ou non, il suffit de choisir seulement des sommets connectés par une arrête. De plus, on remarque que le backtrack ne pourra être effectué que dans une composante connexe du graphe. L’algorithme finira donc plus vite lorsque la zone de départ est dans une composante connexe qui contient peu de sommets. 

Avec cette modélisation, le problème est réduit à un autre problème déjà résolu. On applique donc un algorithme de backtracking qui cherche un plus court chemin entre deux sommets dans un graphe. 

<a name="_page3_x68.00_y225.92"></a>2.  Analyse de solutions pour l’IHM 

Pour réaliser l’éditeur de carte, il serait souhaitable d’avoir une interface colorée, simple à prendre en main, qui minimise le nombre d’interaction à réaliser. Pour avoir une solution intuitive au niveau de la coloration de la carte, il est possible de s’inspirer des couleurs utilisés sur les cartes géographiques qui représentent les hauteurs. La convention la plus courante semble être la suivante :  

- Le point le plus bas est représenté par du vert foncé 
- Le point médiant est représenté par un jaune clair 
- Le point le plus haut est représenté par un marron foncé 
- Les autres sont des dégradés proportionnels à la position entre deux des points ci-dessus 

Pour l’affichage de la carte, il est possible de s’inspirer des jeux-vidéos en utilisant un moteur de rendu 2D qui fonctionne avec des tuiles. Avec cette méthode, chaque case est représentée par une tuile, qui a une texture choisie. Cette méthode permet de calculer des tuiles à afficher pour donner l’impression qu’il n’y a qu’une seule image. 

Avec ce système, l’édition pourra se faire directement sur l’affichage de la carte, en s’inspirant des éditeurs graphiques des moteurs de rendu. L’utilisateur n’aura alors qu’à faire glisser sa souris en maintenant le click principal enfoncé pour placer ses tuiles. 

3. Conception<a name="_page4_x68.00_y70.92"></a> du projet 
1. Représentation<a name="_page4_x68.00_y114.92"></a> du graphe 

Comme expliqué précédemment, il a été choisi de représenter le problème par un graphe orienté. Pour réaliser cette structure en Java. Nous allons utiliser 3 objets différents. Des **sommets**, des **arrêtes** et le **graphe** en lui-même. 

Une arrête contient un nœud de départ et un nœud de d’arrivé, ainsi que le poids de l’arrête. Un sommet contient la liste des arrêtes qui ont pour départ ce sommet. Enfin, le graphe contient la liste de tous les sommets qui représentent le graphe. On obtient la modélisation UML suivante :  

![](Aspose.Words.00b245a1-3ec5-421a-a72f-ea78ee41fb1a.005.png)

*Figure 2 - Diagramme UML de la structure de graphe* 

2. Recherche<a name="_page5_x68.00_y70.92"></a> du plus court chemin 

Une fois que le graphe est modélisé, il est possible d’implémenter l’algorithme de **backtrack** pour rechercher un plus court chemin dans ce graphe. Pour implémenter cet algorithme, on utilise une structure de pile.  

On commence par mettre dans la **pile** le sommet de départ, puis on se déplace de sommet et sommet jusqu’à arriver au sommet d’arrivée ou que l’on est bloqué. Lorsque l’on est bloqué, on revient d’un cran en arrière puis on essaye de se déplacer sur un autre nœud. Quand on trouve une solution, on définit le coût maximal du chemin étant égal au coût de la solution, puis on met en cache cette solution. Par la suite, on revient également en arrière lorsque le coût du chemin qui est en train d’être calculé devient supérieur au coût du dernier chemin valide. 

L’algorithme se fini quand la pile devient vide. On a alors deux possibilités, soit un chemin a été trouvé, dans ce cas, on a le meilleur chemin valide en cache. Soit aucun chemin n’existe entre ces deux sommets, dans ce cas, il n’y a rien en cache. 

![](Aspose.Words.00b245a1-3ec5-421a-a72f-ea78ee41fb1a.006.jpeg)

*Figure 3 - Exemple de plus court chemin trouvé* 

3. Fonctionnement<a name="_page6_x68.00_y70.92"></a> du moteur de rendu 

Afin d’avoir un rendu plus esthétique mais également pour pallier aux limitations de la librairie Swing. Le projet intègre un petit moteur de rendu qui fonctionne à base de tuiles. L’avantage des tuiles est qu’il permet de réaliser des textures ‘connectés’.  Ce dernier fonctionne avec un système de couches. Il commence par calculer une texture pour représenter la hauteur  la plus basse, puis réaliser une texture pour chaque couche supérieure. Enfin, il superpose les textures de chaque hauteur pour réaliser le rendu final. Pour pouvoir représenter presque tous les cas possibles de connexions entre les textures, il est nécessaire d’utiliser 32 tuiles. 16 tuiles pour les connexions sur les cotés et 16 tuiles pour représenter les connexions sur les coins. On peut voir sur l’image suivante les 16 textures de tuiles possibles pour les liaisons adjacentes. 

![](Aspose.Words.00b245a1-3ec5-421a-a72f-ea78ee41fb1a.007.png)

*Figure 4 - Textures des tuiles* 

Ce moteur de rendu est composé de divers composants, qui sont arrangés de la manière suivante :  

![](Aspose.Words.00b245a1-3ec5-421a-a72f-ea78ee41fb1a.008.png)

*Figure 5 - Architecture du moteur de rendu* 

L’interface **Renderer**, permet de créer des objets qui seront rendus sous forme d’image, avec la méthode render. L’interface **Placeable**, permet de créer des objets qui peuvent-être placés dans une vue. La méthode place, place le rendu de l’objet dans l’image passé en paramètre. 

La  classe  **TiledRenderer**,  permet  de  faire  du  rendu  de  tuiles,  elle  possède  des  méthodes utilitaires  qui  facilitent  le  placement  des  tuiles  pour  faire  le  rendu  de  l’image.  Enfin,  la  classe **OverlayRenderer**, contient une liste d’objets qui implémentent ‘**Placeable’**, il trace tous ces objets sur une même image, ce qui permet de superposer les différentes couches pour faire le rendu. Voici le diagramme final, avec les implémentations. 

![](Aspose.Words.00b245a1-3ec5-421a-a72f-ea78ee41fb1a.009.png)

*Figure 6 - Implémentations pour le rendu* 

4. Interface<a name="_page8_x68.00_y70.92"></a> utilisateur 

L’interface utilisateur seras composée de deux fenêtres, la première permettra de choisir une carte à charger parmi toutes les cartes disponibles.  La seconde, permettra d’éditer et d’effectuer des calculs sur une carte. Pour chaque fenêtre, on réalisera deux classes, la première servira à gérer la partie visuelle, des éléments dans la fenêtre. La seconde servira à traiter les données lorsqu’une action est effectuée dans la fenêtre. 

Pour l’affichage de la liste des cartes disponibles, on affiche une miniature de la carte à charger, cela permet d’identifier plus facilement les cartes. On dispose ensuite de deux boutons, l’un pour supprimer la carte, et l’autre pour la charger dans l’éditeur de cartes. Voici le rendu de cette fenêtre : 

![](Aspose.Words.00b245a1-3ec5-421a-a72f-ea78ee41fb1a.010.jpeg)

*Figure 7 - Menu des cartes disponibles* 

La fenêtre d’édition de carte, donne un aperçu de la carte. Elle contient également 2 boutons permettant  de  choisir  si  l’on  souhaite  modifier ou éditer  la  carte. Mais  aussi  un  bouton  pour  la sauvegarder, et un bouton pour rouvrir le menu de la liste des cartes.  

Elle contient également un curseur vertical, qui permet de définir la hauteur des tuiles que l’on souhaite placer dans l’éditeur. Et une case à cocher, qui permet de choisir si l’on souhaite afficher la hauteur des tuiles sur la carte ou non. On peut observer le résultat sur l’image suivante :  

![](Aspose.Words.00b245a1-3ec5-421a-a72f-ea78ee41fb1a.011.jpeg)

*Figure 8 - Fenêtre d'édition de carte* 

4. Performances<a name="_page10_x68.00_y70.92"></a> de l’algorithme 

Le gros problème de l’utilisation du **backtracking**, est sa complexité temporelle. Nous avons déjà ajouté une condition qui évite d’avoir à rechercher des chemins plus grands que la dernière solution. Mais plaçons-nous dans le pire des cas. Si on a une carte de taille **M**×**N**, avec une seule case non accessible et une forte connexité, on a donc à parcourir un graphe qui contient **M**×**N** nœuds, qui permettent chacun d’accéder à 4 directions au plus. Soit **4(M**×**N)** chemins possibles. On a donc une croissance exponentielle du nombre de possibilités. C’est pourquoi, l’algorithme de backtrack n’est pas le plus pertinent à utiliser pour cette application. On aurait pu utiliser l’algorithme de **Djikstra**, qui s’exécute en un temps plus raisonnable qui ne croit pas de manière exponentielle. 

Si l’on souhaite absolument mettre en œuvre un algorithme de backtrack, on peut commencer par déterminer si le départ et l’arrivée sont dans une même composante connexe au préalable pour ne pas avoir à effectuer le backtracking si aucun chemin n’existe. On peut pour cela exécuter l’algorithme de **Warshall** au préalable, pour pouvoir déterminer ensuite s’il existe un chemin entre deux sommets du graphe. 

Voici les résultats que nous pourrions obtenir en termes de complexité pour chaque algorithme :  



|**Algorithme** |**Complexité** |
| - | - |
|Backtracking |4(M×N)|
|Djikstra |5 (M×N) × log(M×N) |
|Warshall |(M×N)3 |

5. Résultats<a name="_page10_x68.00_y420.92"></a> et pistes d’améliorations 

Finalement,  le  programme  obtenu  répond  à  la  problématique  de  départ,  en  respectant  les contraintes qui sont imposées. Les cartes peuvent-être édités directement sur l’application à partir de la souris. De plus, la recherche du plus court chemin est effectuée avec un algorithme de backtracking, comme imposé. Cette solution n’est pas idéale comme vu dans la section sur les performances du programme,  mais  pour  de  petites  cartes,  elle  est  tout  à  fait  utilisable.  On  pourrait  améliorer  ce programme en proposant de configurer la taille de la carte avec des dimensions personnalisées. On pourrait  également  proposer  à  l’utilisateur  de  choisir  des  cases  par  lesquelles  le  chemin  doit obligatoirement passer. De plus, actuellement, il n’est pas imposé de passer par le chemin de plus faible coût qui passe par le moins de case, dans le cas ou il n’y a que de la descente ou du plat, l’algorithme prends  donc  des  chemins  en  zigzag  avant  d’atteindre  l’arrivée.  On  pourrais  donc  ajouter  une fonctionnalité pour ne récupérer que le chemin qui utilise le moins de cases. 
10 
