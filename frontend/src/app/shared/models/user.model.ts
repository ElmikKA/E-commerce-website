export interface User {
    id: string;
    name: string | undefined;
    email: string;
    role: 'client' | 'seller';
    avatar?: string;
  }
  