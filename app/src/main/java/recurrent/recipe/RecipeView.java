package recurrent.recipe;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeView extends Fragment {
    String name = "pizza";
    String instructions = "1. Make the base: Put the flour into a large bowl, then stir in the yeast and salt." +
            " Make a well, pour in 200ml warm water and the olive oil and bring together with a wooden spoon until you have a soft," +
            " fairly wet dough. Turn onto a lightly floured surface and knead for 5 mins until smooth. " +
            "Cover with a tea towel and set aside. You can leave the dough to rise if you like, " +
            "but it’s not essential for a thin crust.\n" +
            "2. Make the sauce: Mix the passata, basil and crushed garlic together, then season to taste. " +
            "Leave to stand at room temperature while you get on with shaping the base.\n" +
            "3. Roll out the dough: If you’ve let the dough rise, give it a quick knead, then split into two balls. " +
            "On a floured surface, roll out the dough into large rounds, about 25cm across, using a rolling pin. " +
            "The dough needs to be very thin as it will rise in the oven. Lift the rounds onto two floured baking sheets.\n" +
            "4. Top and bake: Heat oven to 240C/fan 220C /gas 8. Put another baking sheet or an upturned baking tray in the oven on the top shelf. " +
            "Smooth sauce over bases with the back of a spoon. Scatter with cheese and tomatoes, drizzle with olive oil and season. " +
            "Put one pizza, still on its baking sheet, on top of the preheated sheet or tray." +
            "Bake for 8-10 mins until crisp. Serve with a little more olive oil, and basil leaves if using. " +
            "Repeat step for remaining pizza. As I write this, a portion of Dominic Hawgood’s project Under the Influence is on display at TJ Boulting gallery in London’s West End. The gallery space has been re-floored and re-skinned as a spotless white set for a show that is as much a light installation as a photography exhibition. In this series the photographer uses the whole toolbox of advertising photography, including lighting, digital image manipulation and CGI. He tests the limits of image-making to depict the limits of human experience. These images are drawn from scenes the photographer witnessed in Pentecostal churches in London, as mediated through the ways exorcisms and healings are documented and disseminated by the churches themselves.\n" +
            "\n" +
            "If you just walked in off the street, the experience would be intense but possibly bewildering. The images oscillate between the visual rhetoric of photographic realism and the contrived seduction of advertising photography. They depict a world of sensation in which frozen hands grip, reach and claw, a world of high detail in which chipped nail polish, the down on a girl’s cheek or the texture of institutional carpet provide forensic clues to a mystery that lies somewhere out of frame. With titles for individual images such as, I Command You Get Out, and Rise Up You Are Free, this is the vocabulary and material culture of deliverance: crutches thrown aside, microphones thrust forward, and redemption conferred by the squirt of a spray bottle.\n" +
            "\n" +
            "The experience of the work extends beyond this content to the atmosphere produced in room. Lit from below by the slightly headachy blue glow of hidden LED lights, the five black and white vinyl images of figures appear both to recede into the wall and to float a few millimetres away from it. The matte vinyls are punctuated by two extraordinarily slick objects – sculptural light boxes propped on the floor like a pair of giant iPads. These cast coloured halos into the room and also glow eerily from behind. The overall effect is of slight disorientation. These photographs do not behave as they should. It is as if disembodied screen images have taken on unstable new forms to meet us in the space of the gallery.\n" +
            "\n" +
            "Only parts of Under the Influence can be seen in this installation. As with many contemporary projects, the work exists across several platforms, which may be considered in relation to one another for a fuller picture without necessarily looking to the photographer for an explanation. On the artist’s website vivid text describes what actually happens in the church services – extreme bodily experience is looped through microphones, screens and speakers. At one point, “A woman runs to the front and casts herself down on the floor. She vomits and spits into the tissues placed in front of her. Another runs forward. Stretched out on the floor, they are surrounded by the ministers and cameramen.” Interspersed with the website’s text and images are hypnotic black and white video clips, YouTube extracts posted by the churches reconfigured by the artist into elegant vertical rectangles, unfocused and slowed-down. Too vague to act as documentary evidence (it would not be possible to identify a particular person or activity), they offer a strangely distanced view of physical and spiritual fervour.\n" +
            "\n";
//    String instructions = "As I write this, a portion of Dominic Hawgood’s project Under the Influence is on display at TJ Boulting gallery in London’s West End. The gallery space has been re-floored and re-skinned as a spotless white set for a show that is as much a light installation as a photography exhibition. In this series the photographer uses the whole toolbox of advertising photography, including lighting, digital image manipulation and CGI. He tests the limits of image-making to depict the limits of human experience. These images are drawn from scenes the photographer witnessed in Pentecostal churches in London, as mediated through the ways exorcisms and healings are documented and disseminated by the churches themselves.\n" +
//        "\n" +
//        "If you just walked in off the street, the experience would be intense but possibly bewildering. The images oscillate between the visual rhetoric of photographic realism and the contrived seduction of advertising photography. They depict a world of sensation in which frozen hands grip, reach and claw, a world of high detail in which chipped nail polish, the down on a girl’s cheek or the texture of institutional carpet provide forensic clues to a mystery that lies somewhere out of frame. With titles for individual images such as, I Command You Get Out, and Rise Up You Are Free, this is the vocabulary and material culture of deliverance: crutches thrown aside, microphones thrust forward, and redemption conferred by the squirt of a spray bottle.\n" +
//        "\n" +
//        "The experience of the work extends beyond this content to the atmosphere produced in room. Lit from below by the slightly headachy blue glow of hidden LED lights, the five black and white vinyl images of figures appear both to recede into the wall and to float a few millimetres away from it. The matte vinyls are punctuated by two extraordinarily slick objects – sculptural light boxes propped on the floor like a pair of giant iPads. These cast coloured halos into the room and also glow eerily from behind. The overall effect is of slight disorientation. These photographs do not behave as they should. It is as if disembodied screen images have taken on unstable new forms to meet us in the space of the gallery.\n" +
//        "\n" +
//        "Only parts of Under the Influence can be seen in this installation. As with many contemporary projects, the work exists across several platforms, which may be considered in relation to one another for a fuller picture without necessarily looking to the photographer for an explanation. On the artist’s website vivid text describes what actually happens in the church services – extreme bodily experience is looped through microphones, screens and speakers. At one point, “A woman runs to the front and casts herself down on the floor. She vomits and spits into the tissues placed in front of her. Another runs forward. Stretched out on the floor, they are surrounded by the ministers and cameramen.” Interspersed with the website’s text and images are hypnotic black and white video clips, YouTube extracts posted by the churches reconfigured by the artist into elegant vertical rectangles, unfocused and slowed-down. Too vague to act as documentary evidence (it would not be possible to identify a particular person or activity), they offer a strangely distanced view of physical and spiritual fervour.\n" +
//        "\n";
    Drawable image = getResources().getDrawable(R.drawable.pizza);
    Recipe curr_recipe = new Recipe(name, instructions, image);
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        //in your OnCreate() method
        View view = inflater.inflate(R.layout.recipe_view, parent, false);
        TextView tvname, tvinstructions;
        tvname  = (TextView) view.findViewById(R.id.tvRecipeName);
        tvname.setText(curr_recipe.getName());

        tvinstructions = (TextView) view.findViewById(R.id.tvInstructions);
        tvinstructions.setText(curr_recipe.getInstructions());

        ImageView ivRecipeImage;
        ivRecipeImage = (ImageView) view.findViewById(R.id.ivRecipeImage);
        //ivRecipeImage.setImageBitmap(BitmapFactory.decodeFile(curr_recipe.getImage()));
        //ivRecipeImage.setImageDrawable(R.drawable.pizza);
        //int drawableId = getContext().getResources().getIdentifier("pizza", "drawable", this.getPackageName());
        ivRecipeImage.setImageDrawable(curr_recipe.getImageDrawable());
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }
}
