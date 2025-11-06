CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  role varchar(20) NOT NULL,
  first_name varchar(32) NOT NULL,
  last_name varchar(32) NOT NULL,
  email varchar(255) UNIQUE NOT NULL,
  password_hash varchar(255) NOT NULL,
  locale varchar(10) DEFAULT 'es-MX',
  signup_date timestamptz DEFAULT now(),
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

CREATE TABLE coaches (
  user_id uuid PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
  bio text,
  specialties text[],
  price_cents int,
  verified_status varchar(20) DEFAULT 'pending'
);

CREATE TABLE athlete_profiles (
  user_id uuid PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
  goals text,
  sports text[],
  budget_min int,
  budget_max int
);

CREATE TABLE coach_posts (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  coach_id uuid REFERENCES coaches(user_id) ON DELETE CASCADE,
  type varchar(20),
  body text,
  status varchar(20) DEFAULT 'draft',
  created_at timestamptz DEFAULT now()
);

CREATE TABLE coach_subscriptions (
  coach_id uuid PRIMARY KEY REFERENCES coaches(user_id) ON DELETE CASCADE,
  plan_id varchar(20),
  status varchar(20),
  current_period_end timestamptz
);

CREATE TABLE bookings (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  athlete_id uuid REFERENCES users(id) ON DELETE SET NULL,
  coach_id uuid REFERENCES coaches(user_id),
  start_at timestamptz,
  end_at timestamptz,
  status varchar(20) DEFAULT 'pending',
  created_at timestamptz DEFAULT now()
);

CREATE TABLE refresh_tokens (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id uuid REFERENCES users(id) ON DELETE CASCADE,
  token varchar(512) NOT NULL,
  expires_at timestamptz NOT NULL,
  created_at timestamptz DEFAULT now()
);
