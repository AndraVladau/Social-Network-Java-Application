package com.social.socialnetwork.Controller;


import com.social.socialnetwork.Domain.Mesaj;
import com.social.socialnetwork.Domain.Utilizator;
import com.social.socialnetwork.Utils.Events.ReplyEvent;
import com.social.socialnetwork.Utils.Observer.Observable;
import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.format.DateTimeFormatter;

public class MesajViewController  {

    public Label dataTextEu;
    public Label mesajReplyTextEu;
    public TextFlow textAreaEu;
    public Label dataTextEl;
    public Label mesajReplyTextEl;
    public TextFlow textAreaEl;
    public Button replyButton;
    private Utilizator sender;
    private Mesaj mesaj;

    private UserController parent;

    public void initMesaj(UserController parent, Mesaj mesaj, Utilizator sender){
        this.parent = parent;
        this.mesaj = mesaj;
        this.sender = sender;
        configure();
    }

    private void configure(){
        if(mesaj.getFromUser().equals(sender.getId())){
            //inseamna ca suntem la cel care a trimis mesajul, deci il afisam pe partea lui
            this.textAreaEl.setVisible(false);
            this.textAreaEu.setVisible(true);
//            this.textAreaEu.setText(this.mesaj.getMesajScris());
            this.textAreaEu.getChildren().add(new Text(this.mesaj.getMesajScris()));
            this.dataTextEu.setText(mesaj.getData().format(DateTimeFormatter.ISO_DATE_TIME));
            if(mesaj.getReplyTo()==null){
                mesajReplyTextEu.setVisible(false);
            }
            else{
                mesajReplyTextEu.setVisible(true);
                var mesajulLaCareSeDa = mesaj.getReplyTo().getMesajScris().substring(0, Math.min(20, mesaj.getReplyTo().getMesajScris().length()));
                mesajulLaCareSeDa += "...";
                mesajReplyTextEu.setText(mesajulLaCareSeDa);
            }
        }
        else{
            this.textAreaEu.setVisible(false);
            this.textAreaEl.setVisible(true);
            this.textAreaEl.getChildren().add(new Text(mesaj.getMesajScris()));
            this.dataTextEl.setText(mesaj.getData().format(DateTimeFormatter.ISO_DATE_TIME));
            if(mesaj.getReplyTo()==null){
                mesajReplyTextEl.setVisible(false);
            }
            else{
                mesajReplyTextEl.setVisible(true);
                var mesajScris = mesaj.getReplyTo().getMesajScris();
                var mesajulLaCareSeDa = mesajScris.substring(0, Math.min(20, mesajScris.length())) + "...";
                mesajulLaCareSeDa += "...";
                mesajReplyTextEl.setText(mesajulLaCareSeDa);
            }
        }
    }


    public void handlerReply(ActionEvent actionEvent) {
        this.parent.setReply(mesaj);
    }
}
