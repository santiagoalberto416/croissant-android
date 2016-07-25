package com.croissant.croissant.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.croissant.croissant.Answer;
import com.croissant.croissant.R;
import com.example.santiago.data.AnswerOption;
import com.example.santiago.data.Ask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santiago on 24/07/2016.
 */
public class QuestionsAdapterGraphics extends RecyclerView.Adapter<QuestionsAdapterGraphics.ViewHolder> {
        private List<Ask> items;
        private Context context;

    /**
     * this is the constructor of perceptions
     * @param items this items are all the perceptions from the API
     */

    public QuestionsAdapterGraphics(List<Ask> items, Context context) {
            this.items = items;
            this.context = context;
            }

            /*
            * this method just inflate a element in the recyclerview
            */
        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.graphiclayout, parent, false);
                return new ViewHolder(v);
                }

        /**
         * this method append the one item<deduction> into a layout of amountmessage
         * @param holder this contain the elements of the layout
         * @param position this is the position of the element that is in use
         */
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Ask item = items.get(position);
            String correctAnswer = "";
            int correctP =  item.getTotalCorrectPersons();
            int incorrectP =  item.getTotalWrongPersons();
            List<String> badAnswers = new ArrayList<>();
            for (AnswerOption answer: item.getAnswerOptions()) {
                if(item.getCorrectAnswer()==answer.getIdAnswer()){
                    correctAnswer = answer.getAnswer();
                }else{
                    badAnswers.add(answer.getAnswer());
                }
            }
            holder.texttitle.setText(item.getQuestion());
            holder.correctAnswer.setText(correctAnswer);
            holder.wrongAnswer1.setText(badAnswers.get(0));
            holder.wrongAnswer2.setText(badAnswers.get(1));
            Resources r = context.getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
            //personsCorrect Container

            if(correctP==0 && incorrectP==0){
                holder.correctContainer.setVisibility(View.GONE);
                holder.incorrectContainer.setVisibility(View.GONE);
                holder.messageContainer.setVisibility(View.VISIBLE);
            }else {

                holder.correctContainer.setVisibility(View.VISIBLE);
                holder.incorrectContainer.setVisibility(View.VISIBLE);
                holder.correctBar.setLayoutParams(
                        new TableLayout.LayoutParams(0, Math.round(px), incorrectP));
                holder.emptyBar.setLayoutParams(
                        new TableLayout.LayoutParams(0, Math.round(px), correctP));

                String personWrong = "";
                if (incorrectP > 1)
                    personWrong = incorrectP + " " + context.getResources().getString(R.string.personsWrong);
                else if (incorrectP == 0)
                    personWrong = context.getResources().getString(R.string.noonebad);
                else
                    personWrong = incorrectP + " " + context.getResources().getString(R.string.personWrong);
                holder.personBad.setText(personWrong);

                //persons incorrect
                holder.incorrectBar.setLayoutParams(
                        new TableLayout.LayoutParams(0, Math.round(px), correctP));
                holder.emptyBar2.setLayoutParams(
                        new TableLayout.LayoutParams(0, Math.round(px), incorrectP));
                String personRight = "";
                if (correctP > 1)
                    personRight = correctP + " " + context.getResources().getString(R.string.personsRight);
                else if (correctP == 0)
                    personRight = context.getResources().getString(R.string.noonegood);
                else
                    personRight = correctP + " " + context.getResources().getString(R.string.personRight);
                holder.personRight.setText(personRight);

                holder.messageContainer.setVisibility(View.GONE);
            }



        }

        @Override public int getItemCount() {
                return items.size();
                }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView texttitle;
            public TextView correctAnswer;
            public TextView wrongAnswer1;
            public TextView wrongAnswer2;
            public RelativeLayout emptyBar;
            public RelativeLayout correctBar;
            public RelativeLayout emptyBar2;
            public RelativeLayout incorrectBar;
            public TextView personRight;
            public TextView personBad;
            public RelativeLayout correctContainer;
            public RelativeLayout incorrectContainer;
            public RelativeLayout messageContainer;


            public ViewHolder(View itemView) {
                super(itemView);
                texttitle = (TextView) itemView.findViewById(R.id.titleQuestion);
                correctAnswer = (TextView) itemView.findViewById(R.id.correctAnswer);
                wrongAnswer1 = (TextView) itemView.findViewById(R.id.wrongAnswer1);
                wrongAnswer2 = (TextView) itemView.findViewById(R.id.wrongAnswer2);
                emptyBar = (RelativeLayout)itemView.findViewById(R.id.emptyBar1);
                emptyBar2 = (RelativeLayout)itemView.findViewById(R.id.emptyBar2);
                correctBar = (RelativeLayout)itemView.findViewById(R.id.correctBar);
                incorrectBar = (RelativeLayout)itemView.findViewById(R.id.incorrectBar);
                personBad =(TextView) itemView.findViewById(R.id.personWrong);
                personRight = (TextView)itemView.findViewById(R.id.personRight);
                correctContainer = (RelativeLayout)itemView.findViewById(R.id.correctContainer);
                incorrectContainer = (RelativeLayout)itemView.findViewById(R.id.incorrectContainer);
                messageContainer = (RelativeLayout)itemView.findViewById(R.id.messageContainer);
            }
        }
}
