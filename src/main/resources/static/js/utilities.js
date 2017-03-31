/** Check that start and end dates are valid.
 *  Returns an array with error messages.
 */
function checkDates(startDate, endDate) {

    var dateFormat = 'YYYY-MM-DD';
    var errors = [];
    var startDateIsValid = moment(startDate, dateFormat).isValid();
    var endDateIsValid = moment(endDate, dateFormat).isValid();

    if (!startDateIsValid) {
        errors.push("Erreur de saisie dans la date de début");
    }

    if (!endDateIsValid) {
        errors.push("Erreur de saisie dans la date de fin");
    }

    if (startDateIsValid && endDateIsValid && moment(endDate).isBefore(startDate)) {
        errors.push("Erreur de saisie la date de début est plus grande que date de fin");
    }
    return errors;
}

/** Display a custom blue log in the console*/
function displayCustomConsoleLog(message) {
    console.log('%c' + message, "color: blue");
}