package com.example.data.local

import com.example.data.model.CategoryEntity
import com.example.data.model.Ingredient
import com.example.data.model.RecipeEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object SeedData {
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val ingredientListType = Types.newParameterizedType(List::class.java, Ingredient::class.java)
    private val ingredientAdapter = moshi.adapter<List<Ingredient>>(ingredientListType)
    private val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
    private val stringListAdapter = moshi.adapter<List<String>>(stringListType)

    private fun toJsonIng(list: List<Ingredient>): String = ingredientAdapter.toJson(list)
    private fun toJsonInst(list: List<String>): String = stringListAdapter.toJson(list)

    val defaultCategories = listOf(
        CategoryEntity("cat_breakfast", "Breakfast", "Energizing morning favorites", "breakfast_dining"),
        CategoryEntity("cat_lunch", "Lunch", "Wholesome midday meals", "lunch_dining"),
        CategoryEntity("cat_dinner", "Dinner", "Comforting family dinners", "dinner_dining"),
        CategoryEntity("cat_desserts", "Desserts", "Sweet treats and pastries", "cake"),
        CategoryEntity("cat_snacks", "Snacks", "Quick bites and appetizers", "cookie"),
        CategoryEntity("cat_vegetarian", "Vegetarian", "Plant-based flavorful dishes", "eco"),
        CategoryEntity("cat_non_veg", "Non-Vegetarian", "Succulent poultry, meats & seafood", "restaurant"),
        CategoryEntity("cat_beverages", "Beverages", "Refreshing drinks and teas", "local_cafe")
    )

    fun getInitialRecipes(): List<RecipeEntity> = listOf(
        // 1. Butter Chicken (North Indian)
        RecipeEntity(
            id = "seed_1",
            title = "Authentic Butter Chicken (Murgh Makhani)",
            description = "Tender marinated chicken pieces simmered in a velvety aromatic tomato, butter, and cream sauce infused with fenugreek.",
            imageUrl = "https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?auto=format&fit=crop&w=800&q=80",
            cuisine = "Indian",
            category = "Dinner",
            dietaryPreference = "Non-Vegetarian",
            difficulty = "Medium",
            prepTimeMinutes = 20,
            cookTimeMinutes = 30,
            servings = 4,
            isFavorite = true,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Chicken thighs (boneless, cubed)", 600.0, "g"),
                Ingredient("Yogurt", 0.5, "cup"),
                Ingredient("Ginger-garlic paste", 2.0, "tbsp"),
                Ingredient("Garam masala", 1.5, "tsp"),
                Ingredient("Kashmiri red chili powder", 2.0, "tsp"),
                Ingredient("Butter", 50.0, "g"),
                Ingredient("Tomato puree", 2.0, "cups"),
                Ingredient("Heavy cream", 0.5, "cup"),
                Ingredient("Kasuri methi (fenugreek leaves)", 1.0, "tbsp"),
                Ingredient("Salt", 1.0, "tsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Marinate chicken cubes with yogurt, 1 tbsp ginger-garlic paste, chili powder, and salt for 30 minutes.",
                "Pan-sear the marinated chicken in a skillet with 1 tbsp butter over medium-high heat until lightly charred (6-8 minutes). Set aside.",
                "In the same pan, melt remaining butter and sauté 1 tbsp ginger-garlic paste for 1 minute.",
                "Pour in tomato puree, garam masala, and chili powder. Simmer gently for 12-15 minutes until oil separates.",
                "Stir in heavy cream and crushed kasuri methi until smooth and velvety.",
                "Add seared chicken, simmer for 5 minutes, garnish with coriander, and serve with hot garlic naan."
            ))
        ),

        // 2. Masala Dosa (South Indian)
        RecipeEntity(
            id = "seed_2",
            title = "Crispy South Indian Masala Dosa",
            description = "Golden crisp fermented rice crepes wrapped around spiced mustard-seed potato masala, served with coconut chutney.",
            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?auto=format&fit=crop&w=800&q=80",
            cuisine = "Indian",
            category = "Breakfast",
            dietaryPreference = "Vegetarian",
            difficulty = "Medium",
            prepTimeMinutes = 15,
            cookTimeMinutes = 20,
            servings = 4,
            isFavorite = true,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Fermented dosa batter", 3.0, "cups"),
                Ingredient("Boiled potatoes (peeled & mashed)", 4.0, "pcs"),
                Ingredient("Mustard seeds", 1.0, "tsp"),
                Ingredient("Curry leaves", 10.0, "leaves"),
                Ingredient("Onion (thinly sliced)", 1.0, "medium"),
                Ingredient("Green chilies (chopped)", 2.0, "pcs"),
                Ingredient("Turmeric powder", 0.5, "tsp"),
                Ingredient("Ghee or oil", 3.0, "tbsp"),
                Ingredient("Salt", 1.0, "tsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Heat 1 tbsp oil in a pan; add mustard seeds, curry leaves, and green chilies until they splutter.",
                "Add sliced onions and sauté until translucent. Stir in turmeric and salt.",
                "Add mashed boiled potatoes with 2 tbsp water; mix thoroughly and cook for 3 minutes. Potato masala is ready.",
                "Heat a cast iron tawa until water droplets sizzle. Pour a ladle of dosa batter and swirl outward in concentric circles.",
                "Drizzle ghee around the edges and cook on medium heat until golden brown and crisp.",
                "Place potato masala in the center, roll tightly into a cylinder, and serve with coconut chutney."
            ))
        ),

        // 3. Classic Italian Margherita Pizza
        RecipeEntity(
            id = "seed_3",
            title = "Classic Neapolitan Margherita Pizza",
            description = "Hand-stretched artisan pizza crust topped with crushed San Marzano tomatoes, fresh mozzarella balls, and aromatic basil leaves.",
            imageUrl = "https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?auto=format&fit=crop&w=800&q=80",
            cuisine = "Italian",
            category = "Dinner",
            dietaryPreference = "Vegetarian",
            difficulty = "Medium",
            prepTimeMinutes = 20,
            cookTimeMinutes = 10,
            servings = 2,
            isFavorite = true,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Pizza dough ball", 1.0, "ball"),
                Ingredient("San Marzano crushed tomatoes", 0.75, "cup"),
                Ingredient("Fresh Mozzarella (fior di latte)", 150.0, "g"),
                Ingredient("Fresh basil leaves", 8.0, "leaves"),
                Ingredient("Extra virgin olive oil", 1.5, "tbsp"),
                Ingredient("Sea salt", 0.5, "tsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Preheat oven to maximum temperature (450-500°F / 250°C) with a pizza stone inside.",
                "Stretch dough gently on a floured surface by hand from the center outward, maintaining an airy rim.",
                "Spread crushed tomatoes evenly, leaving a 1-inch border. Season lightly with sea salt.",
                "Tear fresh mozzarella and distribute evenly over the sauce.",
                "Bake for 8-10 minutes until cheese bubbles and the crust develops charred leopard spots.",
                "Garnish with fresh basil leaves and a generous drizzle of cold-pressed extra virgin olive oil."
            ))
        ),

        // 4. Creamy Fettuccine Alfredo (Italian)
        RecipeEntity(
            id = "seed_4",
            title = "Creamy Garlic Parmesan Fettuccine",
            description = "Silky ribbons of fettuccine pasta tossed in rich European butter, crushed garlic, and aged Parmigiano Reggiano.",
            imageUrl = "https://images.unsplash.com/photo-1645112411341-6c4fd023714a?auto=format&fit=crop&w=800&q=80",
            cuisine = "Italian",
            category = "Dinner",
            dietaryPreference = "Vegetarian",
            difficulty = "Easy",
            prepTimeMinutes = 10,
            cookTimeMinutes = 15,
            servings = 3,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Fettuccine pasta", 300.0, "g"),
                Ingredient("Butter", 4.0, "tbsp"),
                Ingredient("Garlic (minced)", 3.0, "cloves"),
                Ingredient("Heavy cream", 1.0, "cup"),
                Ingredient("Parmesan cheese (freshly grated)", 1.25, "cups"),
                Ingredient("Fresh parsley (finely chopped)", 2.0, "tbsp"),
                Ingredient("Black pepper", 0.5, "tsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Cook fettuccine in a large pot of salted boiling water until al dente. Reserve 1/2 cup pasta water.",
                "In a large skillet, melt butter over medium heat. Sauté minced garlic for 60 seconds until fragrant.",
                "Pour in heavy cream and bring to a gentle simmer for 3 minutes.",
                "Remove skillet from direct high heat, whisk in freshly grated parmesan until silky and melted.",
                "Toss cooked fettuccine into the sauce, adding reserved pasta water if needed for glossy emulsion.",
                "Season with cracked black pepper and fresh chopped parsley before plating."
            ))
        ),

        // 5. Thai Green Chicken Curry (Asian)
        RecipeEntity(
            id = "seed_5",
            title = "Authentic Thai Green Coconut Curry",
            description = "Fragrant Thai curry loaded with lemongrass, kaffir lime, tender chicken, bamboo shoots, and velvety coconut milk.",
            imageUrl = "https://images.unsplash.com/photo-1455619452474-d2be8b1e70cd?auto=format&fit=crop&w=800&q=80",
            cuisine = "Asian",
            category = "Dinner",
            dietaryPreference = "Non-Vegetarian",
            difficulty = "Easy",
            prepTimeMinutes = 15,
            cookTimeMinutes = 15,
            servings = 4,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Chicken breast (sliced thin)", 450.0, "g"),
                Ingredient("Thai green curry paste", 3.0, "tbsp"),
                Ingredient("Coconut milk (full-fat)", 400.0, "ml"),
                Ingredient("Bamboo shoots", 0.5, "cup"),
                Ingredient("Bell pepper (sliced)", 1.0, "pcs"),
                Ingredient("Fish sauce", 1.5, "tbsp"),
                Ingredient("Palm sugar or brown sugar", 1.0, "tsp"),
                Ingredient("Thai basil leaves", 0.5, "cup")
            )),
            instructionsJson = toJsonInst(listOf(
                "Scoop 3 tablespoons of coconut cream from the top of the can into a wok over medium heat.",
                "Add green curry paste and fry vigorously for 2 minutes until aromatic oils release.",
                "Add sliced chicken and sear until cooked through on the outside (3-4 minutes).",
                "Pour in remaining coconut milk, bamboo shoots, and bell peppers. Simmer gently for 8 minutes.",
                "Season with fish sauce and palm sugar. Adjust seasoning to achieve a balance of salty, sweet, and spicy.",
                "Turn off heat, stir in fresh Thai basil leaves, and serve with fragrant jasmine rice."
            ))
        ),

        // 6. Japanese Chicken Katsu Curry
        RecipeEntity(
            id = "seed_6",
            title = "Crispy Japanese Chicken Katsu Curry",
            description = "Golden panko-crusted crispy fried chicken cutlet served over rich, mildly spiced Japanese curry gravy and steamed rice.",
            imageUrl = "https://images.unsplash.com/photo-1569058242253-92a9c755a0ec?auto=format&fit=crop&w=800&q=80",
            cuisine = "Asian",
            category = "Lunch",
            dietaryPreference = "Non-Vegetarian",
            difficulty = "Medium",
            prepTimeMinutes = 20,
            cookTimeMinutes = 25,
            servings = 3,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Chicken breasts (flattened)", 2.0, "pcs"),
                Ingredient("Japanese panko breadcrumbs", 1.5, "cups"),
                Ingredient("Egg (beaten)", 1.0, "pcs"),
                Ingredient("All-purpose flour", 0.5, "cup"),
                Ingredient("Japanese curry roux block", 3.0, "cubes"),
                Ingredient("Carrot (cubed)", 1.0, "pcs"),
                Ingredient("Onion (diced)", 1.0, "pcs"),
                Ingredient("Cooking oil for frying", 2.0, "cups")
            )),
            instructionsJson = toJsonInst(listOf(
                "Sauté diced onions and carrots in a saucepan with 1 tbsp oil until onions are translucent.",
                "Add 2.5 cups water, bring to boil, and simmer for 10 minutes until carrots are tender.",
                "Dissolve Japanese curry cubes into the broth, stirring continuously until thickened into a glossy curry sauce.",
                "Dredge chicken cutlets in flour, dip in beaten egg, and press firmly into panko breadcrumbs.",
                "Deep fry or shallow fry cutlets at 350°F (175°C) for 5-6 minutes per side until deeply golden and crunchy.",
                "Slice katsu cutlet into strips, lay over fluffy rice, and ladle hot aromatic curry alongside."
            ))
        ),

        // 7. Paneer Tikka (North Indian Snack)
        RecipeEntity(
            id = "seed_7",
            title = "Smoky Tandoori Paneer Tikka",
            description = "Succulent cottage cheese cubes, crisp bell peppers, and red onions skewered and char-grilled with fragrant tandoori spices.",
            imageUrl = "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?auto=format&fit=crop&w=800&q=80",
            cuisine = "Indian",
            category = "Snacks",
            dietaryPreference = "Vegetarian",
            difficulty = "Easy",
            prepTimeMinutes = 20,
            cookTimeMinutes = 15,
            servings = 4,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Paneer (cottage cheese cubes)", 400.0, "g"),
                Ingredient("Bell peppers (cubed)", 2.0, "pcs"),
                Ingredient("Red onion (petals)", 1.0, "large"),
                Ingredient("Hung curd or Greek yogurt", 0.75, "cup"),
                Ingredient("Mustard oil", 2.0, "tbsp"),
                Ingredient("Chaat masala", 1.0, "tsp"),
                Ingredient("Kashmiri chili powder", 1.5, "tsp"),
                Ingredient("Lemon juice", 1.0, "tbsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Whisk hung curd with mustard oil, chili powder, chaat masala, lemon juice, and salt to create marinade.",
                "Gently fold paneer cubes, onion petals, and bell pepper chunks into the marinade. Rest for 20 minutes.",
                "Thread paneer and vegetables alternatively onto skewers.",
                "Grill on high heat in an oven (220°C) or pan-sear on a cast iron skillet for 10-12 minutes, turning occasionally until charred.",
                "Sprinkle chaat masala and fresh mint chutney before serving hot."
            ))
        ),

        // 8. Fluffy Blueberry Buttermilk Pancakes (Breakfast)
        RecipeEntity(
            id = "seed_8",
            title = "Fluffy Golden Blueberry Pancakes",
            description = "Cloud-like buttermilk pancakes bursting with juicy sweet blueberries, served with pure amber maple syrup.",
            imageUrl = "https://images.unsplash.com/photo-1528207776546-365bb710ee93?auto=format&fit=crop&w=800&q=80",
            cuisine = "American",
            category = "Breakfast",
            dietaryPreference = "Vegetarian",
            difficulty = "Easy",
            prepTimeMinutes = 10,
            cookTimeMinutes = 12,
            servings = 4,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("All-purpose flour", 2.0, "cups"),
                Ingredient("Buttermilk", 1.75, "cups"),
                Ingredient("Fresh blueberries", 1.0, "cup"),
                Ingredient("Baking powder", 2.0, "tsp"),
                Ingredient("Eggs", 2.0, "pcs"),
                Ingredient("Melted butter", 3.0, "tbsp"),
                Ingredient("Granulated sugar", 2.0, "tbsp"),
                Ingredient("Vanilla extract", 1.0, "tsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Whisk dry ingredients (flour, baking powder, sugar, pinch of salt) together in a mixing bowl.",
                "In a separate bowl, whisk buttermilk, eggs, melted butter, and vanilla extract.",
                "Fold wet mixture into dry ingredients gently until just combined (small lumps are normal for fluffiness).",
                "Heat a non-stick griddle over medium-low heat with a dab of butter.",
                "Pour 1/4 cup batter for each pancake, scatter fresh blueberries on top, and flip when bubbles pop (2-3 mins).",
                "Cook for 2 more minutes until golden brown; serve stacked with maple syrup."
            ))
        ),

        // 9. Classic Italian Tiramisu (Dessert)
        RecipeEntity(
            id = "seed_9",
            title = "Traditional Venetian Tiramisu",
            description = "Espresso-soaked savoiardi ladyfingers layered with rich mascarpone zabaglione cream and dusted with Dutch cocoa.",
            imageUrl = "https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?auto=format&fit=crop&w=800&q=80",
            cuisine = "Italian",
            category = "Desserts",
            dietaryPreference = "Vegetarian",
            difficulty = "Medium",
            prepTimeMinutes = 25,
            cookTimeMinutes = 0,
            servings = 6,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Savoiardi ladyfingers", 24.0, "pcs"),
                Ingredient("Mascarpone cheese", 500.0, "g"),
                Ingredient("Fresh brewed espresso (cooled)", 1.5, "cups"),
                Ingredient("Egg yolks", 4.0, "pcs"),
                Ingredient("Sugar", 0.5, "cup"),
                Ingredient("Heavy whipping cream", 1.0, "cup"),
                Ingredient("Dark cocoa powder", 2.0, "tbsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Beat egg yolks and sugar in a heatproof bowl set over simmering water until pale and thick (5 mins). Cool slightly.",
                "Fold room-temperature mascarpone into the egg mixture until completely smooth.",
                "In another bowl, whip heavy cream to stiff peaks; gently fold into the mascarpone cream.",
                "Quickly dip ladyfingers into cooled espresso (1-2 seconds per side) and arrange in an even layer in a dish.",
                "Spread half the mascarpone cream over ladyfingers. Repeat with a second layer of soaked biscuits and remaining cream.",
                "Dust generously with cocoa powder and chill in the refrigerator for at least 4 hours before serving."
            ))
        ),

        // 10. Quick 15-Minute Garlic Butter Shrimp
        RecipeEntity(
            id = "seed_10",
            title = "15-Minute Sizzling Garlic Butter Shrimp",
            description = "Plump prawns pan-seared in rich browned butter with minced garlic, crushed red pepper, and fresh lemon zest.",
            imageUrl = "https://images.unsplash.com/photo-1559742811-82286364ceaf?auto=format&fit=crop&w=800&q=80",
            cuisine = "Mediterranean",
            category = "Dinner",
            dietaryPreference = "Non-Vegetarian",
            difficulty = "Easy",
            prepTimeMinutes = 5,
            cookTimeMinutes = 10,
            servings = 3,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Shrimp or prawns (peeled & deveined)", 500.0, "g"),
                Ingredient("Butter", 3.0, "tbsp"),
                Ingredient("Olive oil", 1.0, "tbsp"),
                Ingredient("Garlic (thinly sliced)", 6.0, "cloves"),
                Ingredient("Red chili flakes", 0.5, "tsp"),
                Ingredient("Lemon juice", 2.0, "tbsp"),
                Ingredient("Fresh parsley (chopped)", 2.0, "tbsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Pat raw shrimp thoroughly dry with paper towels and season with salt and black pepper.",
                "Heat olive oil and 1 tbsp butter in a large skillet over high heat until sizzling hot.",
                "Sear shrimp in a single layer for 1.5 minutes without stirring until pink underneath. Flip.",
                "Lower heat to medium, add sliced garlic, red pepper flakes, and remaining butter.",
                "Sauté for 2 minutes as butter foams and garlic turns golden and sweet.",
                "Drizzle fresh lemon juice and toss with fresh chopped parsley. Serve immediately with crusty sourdough."
            ))
        ),

        // 11. Hyderabadi Vegetable Biryani (Indian)
        RecipeEntity(
            id = "seed_11",
            title = "Royal Hyderabadi Dum Veg Biryani",
            description = "Fragrant long-grain basmati rice layered with spiced vegetables, caramelized onions, saffron milk, and fresh mint.",
            imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?auto=format&fit=crop&w=800&q=80",
            cuisine = "Indian",
            category = "Dinner",
            dietaryPreference = "Vegetarian",
            difficulty = "Hard",
            prepTimeMinutes = 25,
            cookTimeMinutes = 35,
            servings = 5,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Basmati rice (aged)", 2.0, "cups"),
                Ingredient("Mixed vegetables (carrots, beans, peas, potatoes)", 2.5, "cups"),
                Ingredient("Crispy fried onions (birista)", 1.0, "cup"),
                Ingredient("Yogurt", 0.75, "cup"),
                Ingredient("Biryani masala powder", 2.0, "tbsp"),
                Ingredient("Saffron strands dissolved in warm milk", 3.0, "tbsp"),
                Ingredient("Fresh mint and coriander leaves", 0.5, "cup"),
                Ingredient("Ghee", 3.0, "tbsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Boil basmati rice with whole spices (cardamom, cloves, bay leaf) until 70% cooked (about 6 minutes). Drain.",
                "Marinate diced vegetables in yogurt, biryani masala, ginger-garlic paste, half the mint, and salt for 15 minutes.",
                "Cook vegetable gravy in a heavy-bottom pot until 80% tender.",
                "Layer the partially cooked basmati rice over the spiced vegetables.",
                "Top with saffron milk, ghee, crispy fried onions, fresh coriander, and mint.",
                "Seal the pot tightly with dough or aluminum foil and cook on low heat ('dum') for 20 minutes."
            ))
        ),

        // 12. Vietnamese Fresh Spring Rolls (Asian)
        RecipeEntity(
            id = "seed_12",
            title = "Vietnamese Rice Paper Spring Rolls",
            description = "Crisp translucent rice paper rolls filled with vermicelli noodles, fresh herbs, cucumbers, and creamy peanut dipping sauce.",
            imageUrl = "https://images.unsplash.com/photo-1534422298391-e4f8c172dddb?auto=format&fit=crop&w=800&q=80",
            cuisine = "Asian",
            category = "Snacks",
            dietaryPreference = "Vegan",
            difficulty = "Easy",
            prepTimeMinutes = 20,
            cookTimeMinutes = 5,
            servings = 4,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Rice paper wrappers", 10.0, "sheets"),
                Ingredient("Cooked rice vermicelli noodles", 1.5, "cups"),
                Ingredient("Cucumber (julienned)", 1.0, "pcs"),
                Ingredient("Carrot (shredded)", 1.0, "pcs"),
                Ingredient("Fresh mint leaves", 0.5, "cup"),
                Ingredient("Fresh Thai basil & cilantro", 0.5, "cup"),
                Ingredient("Peanut butter (for dipping sauce)", 0.5, "cup"),
                Ingredient("Hoisin sauce", 2.0, "tbsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Whisk peanut butter, hoisin sauce, 1 tbsp lime juice, and 3 tbsp warm water to make dipping sauce.",
                "Dip a sheet of rice paper in a shallow dish of lukewarm water for 5 seconds until pliable, then lay flat on a board.",
                "Layer vermicelli noodles, shredded carrots, cucumber matchsticks, and herbs in the bottom third of the wrapper.",
                "Fold the bottom edge over the filling, tuck in both sides tightly, and roll upward like a burrito.",
                "Slice each roll in half diagonally and serve chilled with savory peanut sauce."
            ))
        ),

        // 13. Guacamole & Fresh Pico De Gallo (Mexican Snack)
        RecipeEntity(
            id = "seed_13",
            title = "Zesty Authentic Mexican Guacamole",
            description = "Chunky ripe Hass avocados mashed with lime juice, diced jalapeño, Roma tomatoes, cilantro, and sea salt.",
            imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=800&q=80",
            cuisine = "Mexican",
            category = "Snacks",
            dietaryPreference = "Vegan",
            difficulty = "Easy",
            prepTimeMinutes = 10,
            cookTimeMinutes = 0,
            servings = 4,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Ripe Hass avocados", 3.0, "pcs"),
                Ingredient("Fresh lime juice", 2.0, "tbsp"),
                Ingredient("Roma tomatoes (seeded & diced)", 2.0, "pcs"),
                Ingredient("Red onion (finely minced)", 0.25, "cup"),
                Ingredient("Jalapeño pepper (seeded & minced)", 1.0, "pcs"),
                Ingredient("Cilantro leaves (chopped)", 0.25, "cup"),
                Ingredient("Coarse sea salt", 0.75, "tsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Slice avocados in half, remove pits, and scoop flesh into a molcajete or large ceramic bowl.",
                "Add fresh lime juice and coarse sea salt. Mash with a fork to your desired chunky consistency.",
                "Fold in diced Roma tomatoes, minced red onion, jalapeño, and fresh cilantro.",
                "Taste and adjust lime juice and salt as needed.",
                "Serve immediately with warm, crisp corn tortilla chips."
            ))
        ),

        // 14. Classic French Onion Soup (Dinner)
        RecipeEntity(
            id = "seed_14",
            title = "Rich French Onion Soup Gratinée",
            description = "Deeply caramelized sweet onions in rich beef broth, topped with toasted French baguette and bubbling broiled Gruyère cheese.",
            imageUrl = "https://images.unsplash.com/photo-1547592166-23ac45744acd?auto=format&fit=crop&w=800&q=80",
            cuisine = "French",
            category = "Dinner",
            dietaryPreference = "Non-Vegetarian",
            difficulty = "Medium",
            prepTimeMinutes = 15,
            cookTimeMinutes = 45,
            servings = 4,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Yellow onions (thinly sliced)", 5.0, "large"),
                Ingredient("Butter", 3.0, "tbsp"),
                Ingredient("Beef broth or stock", 6.0, "cups"),
                Ingredient("Dry white wine", 0.5, "cup"),
                Ingredient("Fresh thyme sprigs", 4.0, "sprigs"),
                Ingredient("Gruyère cheese (shredded)", 1.5, "cups"),
                Ingredient("French baguette slices (toasted)", 4.0, "slices")
            )),
            instructionsJson = toJsonInst(listOf(
                "Melt butter in a large Dutch oven. Add sliced onions and cook low and slow for 35-40 minutes until deeply golden brown and sweet.",
                "Deglaze the pan with white wine, scraping up all browned savory bits.",
                "Add beef broth and fresh thyme. Simmer uncovered for 20 minutes.",
                "Ladle hot soup into oven-safe ramekins.",
                "Top each ramekin with a toasted baguette slice and a mound of shredded Gruyère cheese.",
                "Broil under high heat for 3-4 minutes until cheese is melted, bubbling, and golden brown."
            ))
        ),

        // 15. Cold Brew Mango Iced Tea (Beverages)
        RecipeEntity(
            id = "seed_15",
            title = "Refreshing Mango Mint Iced Tea",
            description = "Freshly brewed black tea infused with sweet ripe mango nectar, fresh spearmint sprigs, and lemon slices over ice.",
            imageUrl = "https://images.unsplash.com/photo-1556679343-c7306c1976bc?auto=format&fit=crop&w=800&q=80",
            cuisine = "American",
            category = "Beverages",
            dietaryPreference = "Vegan",
            difficulty = "Easy",
            prepTimeMinutes = 5,
            cookTimeMinutes = 5,
            servings = 4,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Black tea bags", 4.0, "bags"),
                Ingredient("Water (boiling)", 4.0, "cups"),
                Ingredient("Ripe mango puree", 1.0, "cup"),
                Ingredient("Honey or agave syrup", 3.0, "tbsp"),
                Ingredient("Fresh mint leaves", 15.0, "leaves"),
                Ingredient("Lemon (sliced)", 1.0, "pcs"),
                Ingredient("Ice cubes", 2.0, "cups")
            )),
            instructionsJson = toJsonInst(listOf(
                "Steep black tea bags in 4 cups boiling water for 5 minutes. Remove tea bags and let cool.",
                "Stir honey or agave syrup into the warm tea until dissolved.",
                "Stir in smooth mango puree until evenly blended. Chill in refrigerator.",
                "Fill tall glasses with ice cubes, mint leaves, and lemon slices.",
                "Pour chilled mango tea over ice, stir gently, and serve cold."
            ))
        ),

        // 16. Mediterranean Quinoa Salad (Lunch)
        RecipeEntity(
            id = "seed_16",
            title = "Crunchy Mediterranean Quinoa Bowl",
            description = "Fluffy protein-rich quinoa tossed with Kalamata olives, English cucumbers, cherry tomatoes, crumbled feta, and lemon oregano dressing.",
            imageUrl = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=800&q=80",
            cuisine = "Mediterranean",
            category = "Lunch",
            dietaryPreference = "Vegetarian",
            difficulty = "Easy",
            prepTimeMinutes = 15,
            cookTimeMinutes = 15,
            servings = 3,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Quinoa (uncooked)", 1.0, "cup"),
                Ingredient("Cherry tomatoes (halved)", 1.5, "cups"),
                Ingredient("English cucumber (diced)", 1.0, "pcs"),
                Ingredient("Kalamata olives (pitted)", 0.5, "cup"),
                Ingredient("Feta cheese (crumbled)", 0.75, "cup"),
                Ingredient("Extra virgin olive oil", 3.0, "tbsp"),
                Ingredient("Fresh lemon juice", 2.0, "tbsp"),
                Ingredient("Dried oregano", 1.0, "tsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Rinse quinoa under cold water. Simmer with 2 cups water for 15 minutes until water is absorbed. Fluff with a fork and cool.",
                "In a small bowl, whisk olive oil, lemon juice, oregano, salt, and pepper.",
                "In a large salad bowl, combine cooled quinoa, cherry tomatoes, cucumbers, and Kalamata olives.",
                "Pour the lemon oregano dressing over the salad and toss well.",
                "Sprinkle crumbled feta cheese on top and chill for 15 minutes before serving."
            ))
        ),

        // 17. South Indian Filter Coffee (Beverages)
        RecipeEntity(
            id = "seed_17",
            title = "Traditional South Indian Filter Kaapi",
            description = "Strong aromatic dark-roasted chicory coffee decoction frothed with boiling whole milk and served in traditional brass dabarah.",
            imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&w=800&q=80",
            cuisine = "Indian",
            category = "Beverages",
            dietaryPreference = "Vegetarian",
            difficulty = "Easy",
            prepTimeMinutes = 5,
            cookTimeMinutes = 5,
            servings = 2,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("South Indian coffee powder with chicory", 3.0, "tbsp"),
                Ingredient("Water (boiling hot)", 0.75, "cup"),
                Ingredient("Whole milk (fresh)", 1.5, "cups"),
                Ingredient("Sugar", 2.0, "tsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Place coffee powder into the upper chamber of the brass coffee filter and tamp down lightly with the pressing disc.",
                "Pour boiling water into the chamber, cover with lid, and allow thick dark decoction to drip down (10 minutes).",
                "Boil fresh whole milk in a saucepan until frothy and piping hot.",
                "Pour 2 tbsp decoction and 1 tsp sugar into a traditional tumbler or cup.",
                "Pour boiling milk from a height to generate thick aromatic crema foam.",
                "Pour back and forth between dabarah and tumbler once to mix and serve scalding hot."
            ))
        ),

        // 18. Crispy Golden Samosas (Snacks)
        RecipeEntity(
            id = "seed_18",
            title = "Crispy Punjabi Potato & Pea Samosas",
            description = "Flaky carom-seed pastry pockets stuffed with spiced cumin potatoes, sweet green peas, and tangy anardana.",
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?auto=format&fit=crop&w=800&q=80",
            cuisine = "Indian",
            category = "Snacks",
            dietaryPreference = "Vegetarian",
            difficulty = "Hard",
            prepTimeMinutes = 30,
            cookTimeMinutes = 20,
            servings = 6,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("All-purpose flour (maida)", 2.0, "cups"),
                Ingredient("Ajwain (carom seeds)", 0.5, "tsp"),
                Ingredient("Ghee or oil for shortening", 4.0, "tbsp"),
                Ingredient("Boiled potatoes (crumbled)", 4.0, "medium"),
                Ingredient("Green peas", 0.5, "cup"),
                Ingredient("Cumin seeds", 1.0, "tsp"),
                Ingredient("Garam masala & chaat masala", 1.5, "tsp"),
                Ingredient("Oil for deep frying", 3.0, "cups")
            )),
            instructionsJson = toJsonInst(listOf(
                "Rub ghee and ajwain into flour until it resembles breadcrumbs. Knead into a stiff dough with cold water and rest for 30 mins.",
                "Sauté cumin seeds, ginger, green chilies, and peas in a pan. Add crumbled potatoes, spices, and salt. Sauté for 5 minutes.",
                "Divide dough into balls, roll into ovals, and cut in half to create semicircles.",
                "Form each semicircle into a cone with water along the seam, fill with potato stuffing, and crimp the base closed.",
                "Fry samosas in moderately hot oil on low heat for 15 minutes until pastry is blister-free, crunchy, and pale golden.",
                "Serve piping hot with sweet tamarind chutney and spicy green chutney."
            ))
        ),

        // 19. Avocado Toast with Poached Egg (Breakfast)
        RecipeEntity(
            id = "seed_19",
            title = "Artisan Sourdough Avocado & Poached Egg",
            description = "Golden toasted sourdough bread topped with creamy smashed avocado, chili flakes, microgreens, and a runny poached farm egg.",
            imageUrl = "https://images.unsplash.com/photo-1525351484163-7529414344d8?auto=format&fit=crop&w=800&q=80",
            cuisine = "American",
            category = "Breakfast",
            dietaryPreference = "Vegetarian",
            difficulty = "Easy",
            prepTimeMinutes = 5,
            cookTimeMinutes = 5,
            servings = 2,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Artisan sourdough bread slices", 2.0, "thick slices"),
                Ingredient("Ripe avocado", 1.0, "large"),
                Ingredient("Farm fresh eggs", 2.0, "pcs"),
                Ingredient("Red pepper chili flakes", 0.5, "tsp"),
                Ingredient("Extra virgin olive oil", 1.0, "tsp"),
                Ingredient("Flaky sea salt", 0.5, "tsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Toast sourdough slices in a pan with a drop of olive oil until crunchy and golden on both sides.",
                "Bring a small pot of water to a gentle simmer. Swirl water into a whirlpool and gently drop in cracked eggs.",
                "Poach eggs for exactly 3 minutes until whites are set and yolks remain silky and runny.",
                "Mash avocado with lemon juice, salt, and pepper; spread thickly over warm sourdough.",
                "Place a poached egg onto each toast slice.",
                "Season with flaky sea salt, freshly cracked pepper, and red chili flakes."
            ))
        ),

        // 20. Decadent Molten Chocolate Lava Cake (Desserts)
        RecipeEntity(
            id = "seed_20",
            title = "Warm Molten Chocolate Lava Cake",
            description = "Individual dark chocolate cakes with a moist sponge exterior and an irresistibly warm liquid chocolate center.",
            imageUrl = "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&w=800&q=80",
            cuisine = "French",
            category = "Desserts",
            dietaryPreference = "Vegetarian",
            difficulty = "Medium",
            prepTimeMinutes = 15,
            cookTimeMinutes = 12,
            servings = 4,
            ingredientsJson = toJsonIng(listOf(
                Ingredient("Bittersweet dark chocolate (70%)", 170.0, "g"),
                Ingredient("Butter", 0.5, "cup"),
                Ingredient("Powdered sugar", 0.5, "cup"),
                Ingredient("Eggs (large)", 2.0, "pcs"),
                Ingredient("Egg yolks", 2.0, "pcs"),
                Ingredient("All-purpose flour", 6.0, "tbsp"),
                Ingredient("Vanilla extract", 1.0, "tsp")
            )),
            instructionsJson = toJsonInst(listOf(
                "Preheat oven to 425°F (220°C). Butter four 6-ounce ramekins and dust with cocoa powder.",
                "Melt dark chocolate and butter together in a heatproof bowl over simmering water until silky smooth.",
                "Whisk in powdered sugar, then beat in whole eggs, egg yolks, and vanilla extract until glossy.",
                "Gently fold in flour until no white streaks remain.",
                "Divide batter evenly among prepared ramekins and bake for 12 minutes until sides are set but center jiggles.",
                "Run a knife around edges, invert onto plates, dust with powdered sugar, and serve with vanilla bean ice cream."
            ))
        )
    )
}
