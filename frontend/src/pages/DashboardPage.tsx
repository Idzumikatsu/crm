import { useQuery } from '@tanstack/react-query';
import { Line, LineChart, Tooltip, XAxis, YAxis } from 'recharts';
import { useApiFetch } from '../api';

interface Point {
  date: string;
  value: number;
}

export const DashboardPage = () => {
  const apiFetch = useApiFetch();
  const { data } = useQuery<Point[]>({
    queryKey: ['analytics'],
    queryFn: () =>
      apiFetch('/api/analytics')
        .then((r) => r.json())
        .then((d) => d as Point[]),
  });

  return (
    <div>
      {data ? (
        <LineChart width={600} height={300} data={data}>
          <XAxis dataKey="date" />
          <YAxis />
          <Tooltip />
          <Line type="monotone" dataKey="value" stroke="#3b82f6" />
        </LineChart>
      ) : (
        'Loading...'
      )}
    </div>
  );
};
