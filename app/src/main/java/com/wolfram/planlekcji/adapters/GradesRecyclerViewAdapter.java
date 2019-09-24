package com.wolfram.planlekcji.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableList;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.grade.Grade;

import java.util.List;

/**
 * @author Wolfram
 * @date 2019-09-20
 */
public class GradesRecyclerViewAdapter extends ExpandableRecyclerViewAdapter<GradesRecyclerViewAdapter.SubjectVH, GradesRecyclerViewAdapter.GradeVH> {
    public GradesRecyclerViewAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    public void setExpandableList(List<? extends ExpandableGroup> groups){
        this.expandableList = new ExpandableList(groups);
    }

    public void notifyGroupDataChanged() {
        this.expandableList.expandedGroupIndexes = new boolean[this.getGroups().size()];
        for (int i = 0; i < this.getGroups().size(); i++) {
            this.expandableList.expandedGroupIndexes[i] = false;
        }
    }

    @Override
    public SubjectVH onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        return new SubjectVH(v);
    }

    @Override
    public GradeVH onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item, parent, false);
        return new GradeVH(v);
    }

    @Override
    public void onBindChildViewHolder(GradeVH holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Grade g = (Grade) group.getItems().get(childIndex);
        holder.gradeDesc.setText(g.getDescription());
    }

    @Override
    public void onBindGroupViewHolder(SubjectVH holder, int flatPosition, ExpandableGroup group) {
        holder.subjectName.setText(group.getTitle());
    }

    class SubjectVH extends GroupViewHolder {

        private TextView subjectName;
        private View arrow;

        public SubjectVH(View itemView) {
            super(itemView);
            //itemView.setBackgroundColor(Color.parseColor("#CECECE"));
            arrow = itemView.findViewById(R.id.item_subject_arrow);
            subjectName = itemView.findViewById(R.id.item_subject_name);
        }
        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 540, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setStartOffset(0);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            rotate.setInterpolator(arrow.getContext(), android.R.anim.decelerate_interpolator);
            arrow.startAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setStartOffset(0);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            rotate.setInterpolator(arrow.getContext(), android.R.anim.decelerate_interpolator);
            arrow.startAnimation(rotate);
        }
    }

    class GradeVH extends ChildViewHolder {

        private TextView gradeDesc;

        public GradeVH(View itemView) {
            super(itemView);
            gradeDesc = itemView.findViewById(R.id.item_grade_description);
        }
    }

}
