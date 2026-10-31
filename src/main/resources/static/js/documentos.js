document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("document-search-form");

    if (!(form instanceof HTMLFormElement)) {
        return;
    }

    form.addEventListener("submit", (event) => {
        event.preventDefault();

        if (!form.reportValidity()) {
            return;
        }

        const documentId = new FormData(form).get("id_documento");
        const baseUrl = form.dataset.baseUrl;

        if (typeof documentId !== "string" || !baseUrl) {
            return;
        }

        window.location.assign(`${baseUrl}/${encodeURIComponent(documentId)}`);
    });
});
