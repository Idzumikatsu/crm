{{- define "schedule-app.fullname" -}}
{{- printf "%s" .Release.Name -}}
{{- end -}}

{{- define "schedule-app.labels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{- define "schedule-app.serviceAccountName" -}}
{{- if .Values.serviceAccount.create -}}
{{- default (include "schedule-app.fullname" .) .Values.serviceAccount.name -}}
{{- else -}}
{{- default "default" .Values.serviceAccount.name -}}
{{- end -}}
{{- end -}}
