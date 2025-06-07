import { useEffect, useState } from 'react';

export interface Template {
  id?: string;
  code: string;
  lang: string;
  subject: string;
  bodyHtml: string;
}

interface Props {
  template?: Template;
  onClose: () => void;
  onSaved: () => void;
}

export const TemplateDialog = ({ template, onClose, onSaved }: Props) => {
  const [code, setCode] = useState(template?.code ?? '');
  const [lang, setLang] = useState(template?.lang ?? 'en');
  const [subject, setSubject] = useState(template?.subject ?? '');
  const [bodyHtml, setBodyHtml] = useState(template?.bodyHtml ?? '');

  useEffect(() => {
    setCode(template?.code ?? '');
    setLang(template?.lang ?? 'en');
    setSubject(template?.subject ?? '');
    setBodyHtml(template?.bodyHtml ?? '');
  }, [template]);

  const save = () => {
    const body = JSON.stringify({ id: template?.id, code, lang, subject, bodyHtml });
    fetch(`/api/templates${template ? '/' + template.id : ''}`, {
      method: template ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json' },
      body,
    }).then(onSaved);
  };

  return (
    <dialog open className="p-4 rounded bg-white shadow-lg w-96">
      <h2 className="text-lg font-bold mb-2">
        {template ? 'Edit template' : 'New template'}
      </h2>
      <div className="space-y-2">
        <input
          className="border p-1 w-full"
          placeholder="Code"
          value={code}
          onChange={(e) => setCode(e.target.value)}
        />
        <input
          className="border p-1 w-full"
          placeholder="Lang"
          value={lang}
          onChange={(e) => setLang(e.target.value)}
        />
        <input
          className="border p-1 w-full"
          placeholder="Subject"
          value={subject}
          onChange={(e) => setSubject(e.target.value)}
        />
        <textarea
          className="border p-1 w-full h-32"
          placeholder="Body HTML"
          value={bodyHtml}
          onChange={(e) => setBodyHtml(e.target.value)}
        />
      </div>
      <div className="mt-4 space-x-2">
        <button className="px-2 py-1 border" onClick={save}>Save</button>
        <button className="px-2 py-1 border" onClick={onClose}>Cancel</button>
      </div>
    </dialog>
  );
};
