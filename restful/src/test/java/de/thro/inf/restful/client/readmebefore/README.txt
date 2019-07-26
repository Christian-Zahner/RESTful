1.Tests müssen nacheinander ausgeführt werden:
	Reihenfolge ist egal nur nicht alle Tests im Package Client gleichzeitig ausführen sonder einzeln Starten, und warten
	bis diese auch fertig, da Tests sonst parallel ausgeführt werden und aufgrund der @Version ist die absicherung für
	Lost-Updates zu gut und die Tests Failen. Zudem werden gleiche Datensätze verwendet, dies kann ebenfalls zu
	problemen führen(Lost-Update).

2. Kein Docker da ich leider kein Win10Pro habe. Konnte auch leider keinen Key über die Hochschule bekommen.

3. Kein Update von XChanges. Da diese generiert werden beim Tausch ist ein nachträgliches ändern für mich nicht Sinnvoll.
