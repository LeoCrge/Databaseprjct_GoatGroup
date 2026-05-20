package com.project.artconnect.ui;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.util.ServiceProvider;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;



public class ArtistController {
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Discipline> disciplineFilter;
    @FXML
    private TableView<Artist> artistTable;
    @FXML
    private TableColumn<Artist, String> nameColumn;
    @FXML
    private TableColumn<Artist, String> cityColumn;
    @FXML
    private TableColumn<Artist, String> emailColumn;
    @FXML
    private TableColumn<Artist, Integer> yearColumn;

    private final ArtistService artistService = ServiceProvider.getArtistService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));

        disciplineFilter.setItems(FXCollections.observableArrayList(artistService.getAllDisciplines()));
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        Discipline d = disciplineFilter.getValue();
        String dName = (d != null) ? d.getName() : null;
        artistTable.setItems(FXCollections.observableArrayList(artistService.searchArtists(query, dName, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        disciplineFilter.setValue(null);
        refreshTable();
    }

    @FXML
    private void handleAdd(ActionEvent event){
        showArtistDialog(null);
    }




    @FXML
    private void handleDelete(ActionEvent event) {
        Artist selectedArtist = artistTable.getSelectionModel().getSelectedItem();
        if (selectedArtist == null) {
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Confirmation");
        confirmAlert.setHeaderText("Delete Artist");
        confirmAlert.setContentText("Are you sure you want to delete " + selectedArtist.getName() + "?");

        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            artistService.deleteArtist(selectedArtist.getName());
            refreshTable();

        }
    }


    @FXML
    private void handleModify(ActionEvent event) {
        Artist selectedArtist = artistTable.getSelectionModel().getSelectedItem();
        if (selectedArtist == null) {
            return;
        }
        showArtistDialog(selectedArtist);
    }

    private void showArtistDialog(Artist artist) {
        Dialog<Artist> dialog = new Dialog<>();
        dialog.setTitle(artist == null ? "Add Artist" : "Edit Artist");
        dialog.setHeaderText(artist == null ? "Create a new artist" : "Edit artist information");

        // Create form fields
        TextField nameField = new TextField();
        nameField.setPromptText("Artist name");
        if (artist != null) nameField.setText(artist.getName());

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        if (artist != null) emailField.setText(artist.getContactEmail() != null ? artist.getContactEmail() : "");

        TextField cityField = new TextField();
        cityField.setPromptText("City");
        if (artist != null) cityField.setText(artist.getCity() != null ? artist.getCity() : "");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        if (artist != null) phoneField.setText(artist.getPhone() != null ? artist.getPhone() : "");

        TextField websiteField = new TextField();
        websiteField.setPromptText("Website");
        if (artist != null) websiteField.setText(artist.getWebsite() != null ? artist.getWebsite() : "");

        TextField socialMediaField = new TextField();
        socialMediaField.setPromptText("Social Media");
        if (artist != null) socialMediaField.setText(artist.getSocialMedia() != null ? artist.getSocialMedia() : "");

        TextArea bioArea = new TextArea();
        bioArea.setPromptText("Biography");
        bioArea.setWrapText(true);
        bioArea.setPrefRowCount(4);
        if (artist != null) bioArea.setText(artist.getBio() != null ? artist.getBio() : "");

        Spinner<Integer> birthYearSpinner = new Spinner<>(1900, 2025, artist != null && artist.getBirthYear() != null ? artist.getBirthYear() : 2000);
        birthYearSpinner.setPrefWidth(100);

        ComboBox<Discipline> disciplineCombo = new ComboBox<>();
        disciplineCombo.setItems(FXCollections.observableArrayList(artistService.getAllDisciplines()));

        // Create layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("City:"), 0, 2);
        grid.add(cityField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("Website:"), 0, 4);
        grid.add(websiteField, 1, 4);
        grid.add(new Label("Social Media:"), 0, 5);
        grid.add(socialMediaField, 1, 5);
        grid.add(new Label("Birth Year:"), 0, 6);
        grid.add(birthYearSpinner, 1, 6);
        grid.add(new Label("Discipline:"), 0, 7);
        grid.add(disciplineCombo, 1, 7);
        grid.add(new Label("Biography:"), 0, 8);
        grid.add(bioArea, 1, 8);

        dialog.getDialogPane().setContent(grid);

        // Add buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        // Handle save
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Artist result = artist != null ? artist : new Artist();
                result.setName(nameField.getText());
                result.setContactEmail(emailField.getText());
                result.setCity(cityField.getText());
                result.setPhone(phoneField.getText());
                result.setWebsite(websiteField.getText());
                result.setSocialMedia(socialMediaField.getText());
                result.setBio(bioArea.getText());
                result.setBirthYear(birthYearSpinner.getValue());
                result.setActive(true);

                // Add discipline if selected
                if (disciplineCombo.getValue() != null) {
                    result.getDisciplines().clear();
                    result.getDisciplines().add(disciplineCombo.getValue());
                }

                return result;
            }
            return null;
        });

        // Show dialog and process result
        dialog.showAndWait().ifPresent(result -> {
            if (artist == null) {
                artistService.createArtist(result);
            } else {
                artistService.updateArtist(result);
            }
            refreshTable();
        });
    }



        public void refreshTable() {
        artistTable.setItems(FXCollections.observableArrayList(artistService.getAllArtists()));
    }


}
