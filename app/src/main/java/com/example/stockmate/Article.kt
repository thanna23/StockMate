package com.example.stockmate

import android.os.Build
import android.os.Parcelable
import android.os.Parcel
import androidx.annotation.RequiresApi

data class Article(val title: String, var counter: Int = 0, var orderValue: Boolean, var type: Int) : Parcelable {

    constructor(title: String, counter: Int, type: Int) : this(title,counter, false,type) {}

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readInt()
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeInt(counter)
        parcel.writeBoolean(orderValue)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }

        fun getAllArticles(): List<Article> {
            val allArticles = mutableListOf<Article>()

            // Minérales
            allArticles.add(Article("Jus de Pomme BIO", 0, 0))
            allArticles.add(Article("Ramseier", 0, 0))
            allArticles.add(Article("Ginger Beer", 0, 0))
            allArticles.add(Article("San Bitter", 0, 0))
            allArticles.add(Article("Kombucha Hibiscus", 0, 0))
            allArticles.add(Article("Kombucha Gingembre", 0, 0))
            allArticles.add(Article("Limonade Citron", 0, 0))
            allArticles.add(Article("Limonade Orange", 0, 0))
            allArticles.add(Article("Limonade Pamplemousse", 0, 0))
            allArticles.add(Article("Jus de fraise", 0, 0))
            allArticles.add(Article("Jus de tomate", 0, 0))
            allArticles.add(Article("Jus d'orange", 0, 0))
            allArticles.add(Article("Jus de pêche", 0, 0))
            allArticles.add(Article("Jus d'abricot", 0, 0))
            allArticles.add(Article("Jus d'ananas", 0, 0))
            allArticles.add(Article("Jus de poire", 0, 0))
            allArticles.add(Article("Sinalco", 0, 0))
            allArticles.add(Article("Rivella Bleu", 0, 0))
            allArticles.add(Article("Rivella Rouge", 0, 0))
            allArticles.add(Article("Sprite", 0, 0))
            allArticles.add(Article("Fusetea Lemon", 0, 0))
            allArticles.add(Article("Fusetea Peach", 0, 0))
            allArticles.add(Article("Schweppes Tonic" , 0, 0))
            allArticles.add(Article("Schweppes Lemon" , 0, 0))
            allArticles.add(Article("Schweppes Ginger Ale" , 0, 0))
            allArticles.add(Article("Mate", 0, 0))
            allArticles.add(Article("RedBull", 0, 0))
            allArticles.add(Article("Coca", 0, 0))
            allArticles.add(Article("Coca Zero", 0, 0))

            //Bière
            allArticles.add(Article("Magners", 0, 1))
            allArticles.add(Article("BFM Djeronimo", 0, 1))
            allArticles.add(Article("BFM Bats", 0, 1))
            allArticles.add(Article("BFM Torpille", 0, 1))
            allArticles.add(Article("Daura sans gluten", 0, 1))
            allArticles.add(Article("Nanny State", 0, 1))
            allArticles.add(Article("Asahi", 0, 1))
            allArticles.add(Article("Feldschlossen", 0, 1))
            allArticles.add(Article("Heineken 0%", 0, 1))
            allArticles.add(Article("Miller", 0, 1))
            allArticles.add(Article("San Miguel", 0, 1))
            allArticles.add(Article("Desperados", 0, 1))
            allArticles.add(Article("Corona", 0, 1))


            //Vins
            allArticles.add(Article("Therra", 0, 2))
            allArticles.add(Article("Penloup", 0, 2))
            allArticles.add(Article("Météore", 0, 2))
            allArticles.add(Article("Chateau d'aiguihle", 0, 2))
            allArticles.add(Article("St-Saphorin", 0, 2))
            allArticles.add(Article("Mach", 0, 2))
            allArticles.add(Article("Les Fleur du Mal", 0, 2))
            allArticles.add(Article("Maglia", 0, 2))


            //ALcools
            allArticles.add(Article("Lavagulin", 0, 3))
            allArticles.add(Article("Talisker", 0, 3))
            allArticles.add(Article("Oban", 0, 3))
            allArticles.add(Article("Bullet Bourbon", 0, 3))
            allArticles.add(Article("Jack Daniels", 0, 3))
            allArticles.add(Article("Jameson", 0, 3))
            allArticles.add(Article("Campari", 0, 3))
            allArticles.add(Article("Vermouth", 0, 3))
            allArticles.add(Article("Martini Blanc", 0, 3))
            allArticles.add(Article("Martini Rouge", 0, 3))
            allArticles.add(Article("Averna", 0, 3))
            allArticles.add(Article("Suze", 0, 3))
            allArticles.add(Article("Fernet Branca", 0, 3))
            allArticles.add(Article("Ramazzotti amaro", 0, 3))
            allArticles.add(Article("Cynar", 0, 3))
            allArticles.add(Article("Ricard", 0, 3))
            allArticles.add(Article("Pastis", 0, 3))
            allArticles.add(Article("Cachaça", 0, 3))
            allArticles.add(Article("Pisco", 0, 3))
            allArticles.add(Article("Mezcal", 0, 3))
            allArticles.add(Article("Rhum Blanc", 0, 3))
            allArticles.add(Article("Rhum Brun", 0, 3))
            allArticles.add(Article("Rhum 7 años", 0, 3))
            allArticles.add(Article("Diplomatico", 0, 3))
            allArticles.add(Article("Tequila Blanche", 0, 3))
            allArticles.add(Article("Tequila Brune", 0, 3))
            allArticles.add(Article("Tequila Patron", 0, 3))
            allArticles.add(Article("Absolut Vodka", 0, 3))
            allArticles.add(Article("Amaretto", 0, 3))
            allArticles.add(Article("Cointreau", 0, 3))
            allArticles.add(Article("Kahula", 0, 3))
            allArticles.add(Article("Gin Nordic", 0, 3))
            allArticles.add(Article("Gin Nordes", 0, 3))
            allArticles.add(Article("Gin Hendricks", 0, 3))
            allArticles.add(Article("Gin Deux Frères", 0, 3))
            allArticles.add(Article("Gin des Mamies", 0, 3))
            allArticles.add(Article("Gin Bisbino", 0, 3))
            allArticles.add(Article("Gin Monkey", 0, 3))
            allArticles.add(Article("Gin Malfy", 0, 3))
            allArticles.add(Article("Picon", 0, 3))
            allArticles.add(Article("Crème Cassis", 0, 3))

            //Cocktails
            allArticles.add(Article("Prosecco (Caisse)", 0, 4))
            allArticles.add(Article("Ramazzotti Hibiscus", 0, 4))
            allArticles.add(Article("Aperol", 0, 4))
            allArticles.add(Article("St-Germain ", 0, 4))
            allArticles.add(Article("Liqueur de Violette", 0, 4))
            allArticles.add(Article("Italicus", 0, 4))
            allArticles.add(Article("Tonics (Pack)", 0, 4))
            allArticles.add(Article("Jäggermeister", 0, 4))
            allArticles.add(Article("Zenzerello", 0, 4))
            allArticles.add(Article("Sake", 0, 4))
            allArticles.add(Article("Limoncello", 0, 4))
            allArticles.add(Article("Jet 27", 0, 4))
            allArticles.add(Article("Sambuca", 0, 4))
            allArticles.add(Article("Baileys", 0, 4))
            allArticles.add(Article("Gin Bisbino (Box)", 0, 4))

            //Soft 1L
            allArticles.add(Article("Coca", 0, 5))
            allArticles.add(Article("Coca Zero", 0, 5))
            allArticles.add(Article("Sprite/Romanette", 0, 5))
            allArticles.add(Article("Ginger Beer", 0, 5))
            allArticles.add(Article("Jus de Cranberry", 0, 5))
            allArticles.add(Article("Jus d'ananas", 0, 5))
            allArticles.add(Article("Jus d'orange frais", 0, 5))
            allArticles.add(Article("Jusd'orange", 0, 5))

            //Sirops
            allArticles.add(Article("Grenadine", 0, 6))
            allArticles.add(Article("Fraise des Bois", 0, 6))
            allArticles.add(Article("Framboise", 0, 6))
            allArticles.add(Article("Cerise", 0, 6))
            allArticles.add(Article("Fruit de la passion", 0, 6))
            allArticles.add(Article("Rhubarbe", 0, 6))
            allArticles.add(Article("Cassis", 0, 6))
            allArticles.add(Article("Pêche", 0, 6))
            allArticles.add(Article("Citronelle", 0, 6))
            allArticles.add(Article("Fleur de Sureau", 0, 6))
            allArticles.add(Article("Citron vert", 0, 6))
            allArticles.add(Article("Menthe", 0, 6))
            allArticles.add(Article("Kiwi", 0, 6))
            allArticles.add(Article("Orgeat", 0, 6))

            //Fûts
            allArticles.add(Article("Stella", 0, 7))
            allArticles.add(Article("Guiness", 0, 7))
            allArticles.add(Article("Nebuleuse", 0, 7))
            allArticles.add(Article("Matta", 0, 7))
            allArticles.add(Article("Valaisanne", 0, 7))
            allArticles.add(Article("Tuborg", 0, 7))
            allArticles.add(Article("Moretti", 0, 7))
            allArticles.add(Article("Hoegaarden", 0, 7))
            allArticles.add(Article("Punk IPA", 0, 7))

            //Café
            allArticles.add(Article("Lait de vache", 0, 8))
            allArticles.add(Article("Lait d'amande", 0, 8))
            allArticles.add(Article("Lait d'avoine ", 0, 8))
            allArticles.add(Article("Crème (Box)", 0, 8))
            allArticles.add(Article("Chocolats (Pack)", 0, 8))
            allArticles.add(Article("Sucres (Box)", 0, 8))

            return allArticles
        }
    }
}
