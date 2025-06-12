{{- define "schedule-app.fullname" -}}
{{- printf "%s" .Release.Name -}}
{{- end -}}

{{- define "schedule-app.serviceAccountName" -}}
{{- if .Values.serviceAccount.create -}}
{{- default (include "schedule-app.fullname" .) .Values.serviceAccount.name -}}
{{- else -}}
{{- default "default" .Values.serviceAccount.name -}}
{{- end -}}
{{- end -}}
