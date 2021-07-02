package com.project.ensamunity.model;



public enum  VoteType {
    UPVOTE(1), DOWNVOTE(-1),NOVOTE(0),
    ;
private int direction;
    VoteType(int direction) {
    }

}
